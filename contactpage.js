
//changing the preview of image of conatct from add contact page
document.querySelector("#contact_picture").addEventListener('change', function(event){
    let file = event.target.files[0];
    let reader = new FileReader();
    reader.onload = function(){
        document.getElementById("upload_contact_image_preview").src= reader.result;
    };
    reader.readAsDataURL(file);
});