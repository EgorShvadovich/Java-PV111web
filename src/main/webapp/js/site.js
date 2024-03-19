document.addEventListener('DOMContentLoaded', function() {
    var elems = document.querySelectorAll('.modal');
    M.Modal.init(elems, {
        onCloseEnd: onAuthModalClosed
    });

    Date.prototype.toSqlString = function() {
        return `${this.getFullYear()}-${this.getMonth().toString().padStart(2, '0')}-${this.getDate().toString().padStart(2, '0')}`;
    }

    const authButton = document.getElementById("auth-button");
    if(authButton) authButton.addEventListener('click', authButtonClick);

    const newsSubmitButton = document.getElementById("news-submit");
    if(newsSubmitButton) newsSubmitButton.addEventListener('click', newsSubmitClick);

    const newsImgFileInput = document.getElementById("news-file");
    if(newsImgFileInput) newsImgFileInput.onchange = newsImgChange;

    for(let a of document.querySelectorAll("[data-news-id]")) {
        a.addEventListener('click', deleteNewsClick);
    }
});
function deleteNewsClick(e) {
    const newsId = e.target.closest("[data-news-id]").getAttribute("data-news-id");
    if(!newsId) {
        alert("Empty news id");
        return;
    }
    const appContext = window.location.pathname.split('/')[1] ;
    fetch(`/${appContext}/news/?id=${newsId}`, {
        method: 'DELETE'
    }).then(r => r.json()).then(console.log);
    // console.log(newsId);
}
function newsImgChange(e) {
    const [file] = e.target.files;
    if(file) {
        document.getElementById("news-image-preview").src =
            URL.createObjectURL(file);
    }
    else {
        const appContext = window.location.pathname.split('/')[1] ;
        document.getElementById("news-image-preview").src = `/${appContext}/upload/news/no-image.jpg`;
    }
}
function newsSubmitClick() {   // hoisting - "підняття коду"
    const newsTitle = document.getElementById("news-title");
    if(!newsTitle) throw "Element #news-title not found" ;
    const newsDate = document.getElementById("news-date");
    if(!newsDate) throw "Element #news-date not found" ;
    const newsSpoiler = document.getElementById("news-spoiler");
    if(!newsSpoiler) throw "Element #news-spoiler not found" ;
    const newsText = document.getElementById("news-text");
    if(!newsText) throw "Element #news-text not found" ;
    const newsFile = document.getElementById("news-file");
    if(!newsFile) throw "Element #news-file not found" ;

    let isFormValid = true ;

    const title = newsTitle.value.trim() ;
    if( title.length === 0 ) {
        newsTitle.classList.add("invalid");
        isFormValid = false;
    }
    else {
        newsTitle.classList.remove("invalid");
    }

    if(!newsDate.value) {
        newsDate.value = new Date().toSqlString();
    }

    const spoiler = newsSpoiler.value.trim() ;
    if( spoiler.length === 0 ) {
        newsSpoiler.classList.add("invalid");
        isFormValid = false;
    }
    else {
        newsSpoiler.classList.remove("invalid");
    }

    const text = newsText.value.trim() ;
    if( text.length === 0 ) {
        newsText.classList.add("invalid");
        isFormValid = false;
    }
    else {
        newsText.classList.remove("invalid");
    }

    if(newsFile.files.length === 0) {
        document.getElementById("news-file-path").classList.add("invalid");
        isFormValid = false;
    }
    if( isFormValid ) {  // надсилаємо дані
        const formData = new FormData();
        formData.append("news-title", title);
        formData.append("news-date", newsDate.value);
        formData.append("news-spoiler", spoiler);
        formData.append("news-text", text);
        formData.append("news-file", newsFile.files[0]);
        const appContext = window.location.pathname.split('/')[1] ;
        fetch(`/${appContext}/news/`,{
            method: 'POST',
            body: formData
        }).then(r => r.json()).then(j => {
            if( j.status === "success" ) {
                console.log('OK')
            }
            else {
                console.log('NO')
            }
        }) ;
    }
}
function getAuthElements() {
    const authEmailInput = document.getElementById("auth-email");
    if( ! authEmailInput) throw "Element '#auth-email' not found" ;
    const authPasswordInput = document.getElementById("auth-password");
    if( ! authPasswordInput) throw "Element '#auth-password' not found" ;
    const authMessageDiv = document.querySelector(".auth-message");
    if( ! authMessageDiv) throw "Element '.auth-message' not found" ;
    return [authEmailInput, authPasswordInput, authMessageDiv];
}
function authButtonClick() {
    const [authEmailInput, authPasswordInput, authMessageDiv] = getAuthElements();
    const email = authEmailInput.value;
    if(email === "") {
        authMessageDiv.innerText = "Необхідно заповнити поле 'Email'";
        return;
    }
    const password = authPasswordInput.value;
    if(password === "") {
        authMessageDiv.innerText = "Необхідно заповнити поле 'Password'";
        return;
    }
    const appContext = window.location.pathname.split('/')[1] ;
    fetch(`/${appContext}/auth?email=${email}&password=${password}`)
        .then(r => r.json())
        .then(j => {
            if( j.status !== "success" ) {
                authMessageDiv.innerText = "Автентифікацію відхилено";
            }
            else {
                // переходимо в авторизований режим

                // перезавантажуємо сторінку
                window.location.reload();
            }
        }) ;
}

function onAuthModalClosed() {
    const [authEmailInput, authPasswordInput, authMessageDiv] = getAuthElements();
    authEmailInput.value = "";
    authPasswordInput.value = "";
    authMessageDiv.innerText = "";
}

function newsEditClick(){
    const editables = document.querySelectorAll(`[data-editable="true"]`);
    if(editables.length === 0){
        return;
    }
    const isEdit = editables[0].getAttribute("contenteditable");
    if(isEdit){
        let formData = new FormData();
        for(let element of editables){
            element.removeAttribute("contenteditable");
            if(element.getAttribute("initial-content") !== element.innerText){
                console.log("Changes in");
                formData.append(element.getAttribute("data-parameter"),element.innerText);

            }
        }
        if([...formData.keys()].length !== null){

            const dataId = document.querySelector("[data-news-id]");
            if(!dataId) throw "data-news-id not found";
            const newsId = dataId.getAttribute("data-news-edit-id");
            if(!dataId) throw "data-news-edit-id attribute is empty";
            formData.append("id",newsId);
            const appContext = window.location.pathname.split('/')[1] ;
            fetch(`/${appContext}/news/`, {
                method: 'PUT',
                body:formData
            }).then(r => r.json()).then(console.log);
        }
    }
    else{
        for(let element of editables){
            element.setAttribute("contenteditable",true);
            element.setAttribute("initial-content",element.innerText);
        }
    }
}