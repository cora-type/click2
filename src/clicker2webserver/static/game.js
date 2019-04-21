var gold = 0;
var username = "";
var equipmentList = ["shovel", "excavator", "mine"];

var socket = io.connect({transports: ['websocket']});

setupSocket();


function setupSocket() {
    socket.on('connect', function (event) {
        socket.send('Hello Server!');
    });

    socket.on('message', function (event) {
        var gameState = JSON.parse(event);
        document.getElementById("displayGold").innerHTML = gameState['gold'].toFixed(0);
        var equipmentState = gameState['equipment'];
        for (var i in equipmentList) {
            var equipment = equipmentList[i];
            var buttonText = equipmentState[equipment]['buttonText'];
            buttonText = buttonText.replace(/\n/g, "<br/>");
            document.getElementById(equipment).innerHTML = buttonText;
        }
    });
}


function initializeGame(inputUsername) {
    username = inputUsername;

    var html = "";
    for (var i in equipmentList) {
        var equipment = equipmentList[i];
        html += '<button id="' + equipment + '" onclick="buyEquipment(\'' + equipment + '\')">' + equipment + '</button>';
        html += '<br/>';
    }
    document.getElementById("equipmentButtons").innerHTML = html;
    document.getElementById("displayGold").innerHTML = gold;

    socket.emit("register", username);
}


function clickGold() {
    socket.emit("clickGold");
}


function buyEquipment(equipmentID) {
    socket.emit("buy", equipmentID);
}
