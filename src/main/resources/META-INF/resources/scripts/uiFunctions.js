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