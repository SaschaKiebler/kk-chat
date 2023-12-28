function toggleDarkMode() {
    var element = document.body;
    element.classList.toggle("dark-mode");
    if(element.classList.contains("dark-mode")){
        //Store the preference in localStorage
        localStorage.setItem("dark-mode", "enabled");
    } else {
        localStorage.setItem("dark-mode", "disabled");
    }
}

function linkActive(){
    var links = document.getElementsByTagName("a");
    for (var i = 0; i < links.length; i++) {
        if(links[i].href == window.location.href){
            links[i].firstChild.classList.add("active");
        }
    }


}


function toggleConversations() {
    var element = document.getElementsByClassName("conversations")[0];
    element.classList.toggle("hidden");

    var arrow = document.getElementById("arrow");
    arrow.classList.toggle("transform-180");
}