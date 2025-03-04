console.log('script loaded.');
console.log('admin is working');
const baseUrl = "http://localhost:8080";

let current_theme = getTheme();
//initially -->
changeTheme();

//TODO
function changeTheme(){
    //set to webpage
    document.querySelector('html').classList.add(current_theme);

    //set to the listener to change the theme button
    const changeThemeButton =  document.querySelector('#theme_change_btn');
    //change the text of button
    changeThemeButton.querySelector("span").textContent = current_theme == 'light'?'Dark':'Light';
    changeThemeButton.addEventListener("click",()=>{
        const old_theme= current_theme;
        if(current_theme == 'dark'){
            current_theme="light";
        }else{
            current_theme="dark";
        }

        setTheme(current_theme);
        //remove the current theme
        document.querySelector('html').classList.remove(old_theme);
        //set the current theme in html
        document.querySelector('html').classList.add(current_theme);

        //change the text of button
        changeThemeButton.querySelector("span").textContent = current_theme == 'light'?'Dark':'Light';
    });
   
}

//set theme to local storage

function setTheme(theme){
    localStorage.setItem("theme", theme);
}

//get theme from local storage
function getTheme(){
    let theme = localStorage.getItem("theme");
    if(theme){
        return theme;
    }
    return "light";
}

//showing contact details modal
const viewContactModal = document.getElementById('default-modal');
// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'default-modal',
  override: true
};

const contactModal = new Modal(viewContactModal,options,instanceOptions);

function openContactModal(){
    contactModal.show();
}

function closeContactModal(){
    contactModal.hide();
}

async function loadUserData(id){
    //function 
    console.log(id);
    try{
        const data = await(await fetch(`${baseUrl}/api/contacts/${id}`)).json();
        console.log(data);
        document.querySelector("#contact_name").innerHTML= data.name;
        document.querySelector("#contact_email").innerHTML= data.email;
        document.querySelector("#contact_picture").src= data.picture;
        document.querySelector("#contact_phone").innerHTML= data.phone;
        document.querySelector("#contact_address").innerHTML= data.address;
        document.querySelector("#contact_description").innerHTML= data.description;
        document.querySelector("#contact_gender").innerHTML= data.gender;
        document.querySelector("#contact_linkedinlink").innerHTML= data.linkedinlink;
        document.querySelector("#contact_linkedinlink").href= data.linkedinlink;
        document.querySelector("#contact_websitelink").innerHTML= data.websiteLink;
        document.querySelector("#contact_websitelink").href= data.websiteLink;
        const contactFavourite = document.querySelector("#contact_favourite");
        if(data.favourite){
            contactFavourite.innerHTML=
            "<i class='fa-solid fa-star text-yellow-500'></i><i class='fa-solid fa-star text-yellow-500'></i><i class='fa-solid fa-star text-yellow-500'></i><i class='fa-solid fa-star text-yellow-500'></i><i class='fa-solid fa-star text-yellow-500'></i>";
        }else{
            contactFavourite.innerHTML=
            "<i class='fa-solid fa-star'></i><i class='fa-solid fa-star'></i><i class='fa-solid fa-star'></i><i class='fa-solid fa-star'></i><i class='fa-solid fa-star'></i>"
        }
        
        openContactModal();
    }catch(error){
        console.log("Error:  ", error);
    }
    

}


//delete contact taking confirmation
async function deleteContact(name,id){
    Swal.fire({
        title: "Do you want to delete "+name+" ?",
        icon : "warning",
        showCancelButton: true,
        confirmButtonText: "Delete",
        
      }).then((result) => {
        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed) {
            const url = baseUrl+"/user/contacts/delete/"+id;
            window.location.replace(url);
            
        } 
      });
}

    

//delete profile taking confirmation
async function deleteProfile(id){
    Swal.fire({
        title: "Do you want to delete your profile?",
        icon : "warning",
        showCancelButton: true,
        confirmButtonText: "Delete",
        
      }).then((result) => {
        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed) {
            const url2 = baseUrl+"/user/delete/"+id;
            window.location.replace(url2);
            
        } 
      });
}


//delete profile taking confirmation
async function deleteEmail(id){
    Swal.fire({
        title: "Do you want to delete this email?",
        icon : "warning",
        showCancelButton: true,
        confirmButtonText: "Delete",
        
      }).then((result) => {
        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed) {
            const url3 = baseUrl+"/user/emails/deleteEmail/"+id;
            window.location.replace(url3);
            
        } 
      });
}


const viewEmailModal = document.getElementById('default_modal_email');

// options with default values
const optionsEmail = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptionsEmail = {
  id: 'default_modal_email',
  override: true
};

const emailModal = new Modal(viewEmailModal,optionsEmail,instanceOptionsEmail);

function openEmailModal(){
    emailModal.show();
}

function closeEmailModal(){
    emailModal.hide();
}

async function loadEmailData(id){
    try{
        const dataemail = await(await fetch(`${baseUrl}/api/email/${id}`)).json();
        document.querySelector("#emailsubject").innerHTML= dataemail.subject;
        document.querySelector("#emailbody").innerHTML= dataemail.message;
        document.querySelector("#destinationemail").innerHTML=dataemail.destinationemail;
        openEmailModal();
    }catch(error){
        console.log("Error:  ", error);
    }
}

//OTP input design
document.addEventListener("DOMContentLoaded", function () {
    const inputs = document.querySelectorAll(".otp-input");
    
    inputs.forEach((input, index) => {
        input.addEventListener("input", (e) => {
            if (e.inputType !== "deleteContentBackward" && input.value !== "" && index < inputs.length - 1) {
                inputs[index + 1].focus();
            }
        });
        
        input.addEventListener("keydown", (e) => {
            if (e.key === "Backspace" && input.value === "" && index > 0) {
                inputs[index - 1].focus();
            }
        });
    });
});


//timer for taking OTP

let timeLeft = 120;

function updateTimer() {
    let minutes = Math.floor(timeLeft / 60);
    let seconds = timeLeft % 60;
    seconds = seconds < 10 ? '0' + seconds : seconds;
    document.getElementById('countdown').innerText = `0${minutes}:${seconds}`;
    
    if (timeLeft > 0) {
        timeLeft--;
    } else {
        document.getElementById('countdown').innerText = "Time up";
        clearInterval(timerInterval);
        const url4 = baseUrl+"/user/profile";
        window.location.replace(url4);
    }
}

let timerInterval = setInterval(updateTimer, 1000);




