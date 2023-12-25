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
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => response.json()).then(data => {
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