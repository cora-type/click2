import json
import socket
from threading import Thread
from random import randint

from flask import Flask, send_from_directory, request, render_template
from flask_socketio import SocketIO

import eventlet

eventlet.monkey_patch()

app = Flask(__name__)
socket_server = SocketIO(app)

usernameToSid = {}
sidToUsername = {}

scala_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
scala_socket.connect(('localhost', 8000))


def listen_to_scala(the_socket):
    delimiter = "~"
    buffer = ""
    while True:
        buffer += the_socket.recv(1024).decode()
        while delimiter in buffer:
            message = buffer[:buffer.find(delimiter)]
            buffer = buffer[buffer.find(delimiter) + 1:]
            get_from_scala(message)


Thread(target=listen_to_scala, args=(scala_socket,)).start()


def get_from_scala(data):
    message = json.loads(data)
    username = message["username"]
    user_socket = usernameToSid.get(username, None)
    if user_socket:
        socket_server.emit('message', data, room=user_socket)


def send_to_scala(data):
    scala_socket.sendall(json.dumps(data).encode())


@socket_server.on('register')
def got_message(username):
    usernameToSid[username] = request.sid
    sidToUsername[request.sid] = username
    print(username + " connected")
    message = {"username": username, "action": "connected"}
    send_to_scala(message)


@socket_server.on('disconnect')
def got_connection():
    if request.sid in sidToUsername:
        username = sidToUsername[request.sid]
        del sidToUsername[request.sid]
        del usernameToSid[username]
        print(username + " disconnected")
        message = {"username": username, "action": "disconnected"}
        send_to_scala(message)


@socket_server.on('clickGold')
def click_gold():
    username = sidToUsername[request.sid]
    print(username + " clicked gold")
    message = {"username": username, "action": "clickGold"}
    send_to_scala(message)


@socket_server.on('buy')
def buy_equipment(equipmentID):
    username = sidToUsername[request.sid]
    print(username + " trying to buy " + equipmentID)
    message = {"username": username, "action": "buyEquipment", "equipmentID": equipmentID}
    send_to_scala(message)


@app.route('/')
def index():
    return send_from_directory('static', 'index.html')


@app.route('/game', methods=["POST", "GET"])
def game():
    if request.method == "POST":
        username = request.form.get('username')
    else:
        username = "guest" + str(randint(0, 100000))
    return render_template('game.html', username=username)


@app.route('/<path:filename>')
def static_files(filename):
    return send_from_directory('static', filename)


socket_server.run(app, port=8080)
