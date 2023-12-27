function newChat(){
    fetch('/chat/createConversation', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json()).then(data => {

        console.log(data);

        window.location.href = "/chat?conversationId=" + data["id"];
    })
    }


function deleteChat(conversationId){
    fetch('/chat/deleteConversation?conversationId='+conversationId, {
        method: 'GET',
    }).then(response => response.text()).then(data => {
        console.log(data);
    }
        )
}