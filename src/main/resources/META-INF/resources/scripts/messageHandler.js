function loadMessages(conversationId){
        

    fetch('/chat/allMessages?conversationId='+conversationId, {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json'
    }
}).then(response => response.json()).then(data => {
    console.log(data);
    clearMessages();
    data.messages.forEach(message => {
        
        addMessage(message["text"], message["sender"]);
    });
    if(data.name==="new Conversation" && data.messages.length>3){
        fetch('/chat/generateConversationName?conversationId='+conversationId, {
            method: 'GET',
        }).then(response => response.text()).then(data => {
            console.log(data);
            document.getElementById(conversationId).innerHTML = data;
        }
            )
    }
})

}

function clearMessages(){
        var msDiv = document.getElementById("messages");
        msDiv.innerHTML = "";
}




function addMessage(messageText, sender) {
    var messages = document.getElementById("messages");
        var message = document.createElement("div");
        var senderSpan = document.createElement("span");
        senderSpan.classList.add("sender");
        if(sender == "user" || sender == "User"){
            senderSpan.innerHTML = "User: ";
        }
        else{
            senderSpan.innerHTML = "Assistant: ";
    }
        message.appendChild(senderSpan);
        message.classList.add("message");
        messageText = convertMarkdownToHTML(messageText);
        message.innerHTML = message.innerHTML  + messageText;
        messages.appendChild(message);

        return message;
}

function addMessageBefore(messageText, sender) {
    var messages = document.getElementById("messages");
        var message = document.createElement("div");
        var senderSpan = document.createElement("span");
        senderSpan.classList.add("sender");
        if(sender == "user" || sender == "User"){
            senderSpan.innerHTML = "User: ";
        }
        else{
            senderSpan.innerHTML = "Assistant: ";
    }
        message.appendChild(senderSpan);
        message.classList.add("message");
        messageText = convertMarkdownToHTML(messageText);
        message.innerHTML = message.innerHTML  + messageText;
        messages.insertBefore(message, messages.firstChild);

        return message;
}
