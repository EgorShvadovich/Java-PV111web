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
    if(newsSubmitButton) newsSubmitButton.addEventListener('click', newsSubmitButtonClick);

    const newsImgFileInput = document.getElementById("news-file");
    if(newsImgFileInput)newsImgFileInput.onchange = newsImgChange;

});

function newsImgChange(e){
    const [file] = e.target.files;
    console.log(file);
    if(file){
        document.getElementById("news-image-preview").src =
            URL.createObjectURL(file);
    }
    else{
        const appContext = window.location.pathname.split('/')[1];
        document.getElementById("news-image-preview").src =
            appContext + '/upload/news/no-avatar1.png'
    }
}
function authButtonClick() {
    const [authEmailInput,authMessage,authPasswordInput] = getAuthElements();

    const email = authEmailInput.value;
    if (email === "") {

        authMessage.innerText = "Необхідно заповнити поле 'Email'";
        return;
    }
    const password = authPasswordInput.value;
    if (password === "") {
        authMessage.innerText = "Необхідно заповнити поле 'Password'";
        return;
    }
    const appContext = window.location.pathname.split('/')[1];
    fetch(`/${appContext}/auth?email=${email}&password=${password}`)
        .then(r => r.json())
        .then(j => {
            if (j.status !== "success") {
                authMessage.innerText = "Aвтентифікацію відхилено"
            } else {
                window.location.reload();
            }
        });
}

function onAuthModalClosed(){
    const authMessage = document.querySelector(".auth-message");
    if (!authMessage) throw "Element '.authMessage' not found" ;
    authMessage.innerText = "";
}

function getAuthElements(){
    const authEmailInput = document.getElementById("auth-email");
    if (!authEmailInput) throw "Element '#auth-email' not found" ;

    const authMessage = document.querySelector(".auth-message");
    if (!authMessage) throw "Element '.authMessage' not found" ;

    const authPasswordInput = document.getElementById("auth-password");
    if (!authPasswordInput) throw "Element '#auth-password' not found" ;

    return [authEmailInput,authMessage,authPasswordInput];
}

function newsSubmitButtonClick(){
    const newsTitle = document.getElementById("news-title");
    if(!newsTitle) throw "Element news-title not found";


    const newsDate = document.getElementById("news-date");
    if(!newsDate) throw "Element news-date not found";

    const newsSpoiler = document.getElementById("news-spoiler");
    if(!newsSpoiler) throw "Element news-spoiler not found";

    const newsText = document.getElementById("news-text");
    if(!newsText) throw "Element news-text not found";

    const newsFile = document.getElementById("news-file");
    if(!newsFile) throw "Element news-file not found";

    let isFormValid = true;

    const title = newsTitle.value.trim();
    if(title.length === 0){
        newsTitle.classList.add("invalid");
        isFormValid = false;
    }
    else if(title.length > 0 && title.length <=10){
        newsTitle.classList.add("invalid");
        isFormValid = false;
    }
    else{
        newsTitle.classList.remove("invalid");
    }


    if(!newsDate.value){
        newsDate.value = new Date().toSqlString();
    }

    const spoiler = newsSpoiler.value.trim();
    let words = spoiler.split(/\s+/);

    if(spoiler.length === 0){
        newsSpoiler.classList.add("invalid");
        isFormValid = false;
    }
    else if(words.length >= 10){
        newsSpoiler.classList.add("invalid");
        isFormValid = false;
    }
    else{
        newsSpoiler.classList.remove("invalid");
    }

    const text = newsText.value.trim();
    if(text.length === 0){
        newsText.classList.add("invalid");
        isFormValid = false;
    }
    else if(text.length > 0 && text.length <=300){
        newsText.classList.add("invalid");
        isFormValid = false;
    }
    else{
        newsText.classList.remove("invalid");
    }
    if(newsFile.files.length === 0){
        document.getElementById("news-file-path").classList.add("invalid");
        isFormValid = false;
    }
    let regex = /\.(jpg|jpeg|png|gif|bmp)$/i; // Підтримуються розширення jpg, jpeg, png, gif, bmp

    if(!regex.test(newsFile.files)){
        document.getElementById("news-file-path").classList.add("invalid");
        isFormValid = false;
    }
    if(isFormValid){
        const formData = new FormData();
        formData.append("news-title",title);
        formData.append("news-date",newsDate.value);
        formData.append("news-spoiler",spoiler);
        formData.append("news-text",text);
        formData.append("news-file",newsFile.files[0]);

        const appContext = window.location.pathname.split('/')[1];
        fetch(`/${appContext}/news`,{
            method: 'POST',
            body:formData
        }).then(r => r.json()).then(j => {
                if (j.status !== "success") {
                   console.log('OK')
                } else {
                    console.log('NO')
                }
            });
    }
}
