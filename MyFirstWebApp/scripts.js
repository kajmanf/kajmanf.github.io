console.log('hello world')

$('#t1 .sipka').css("border-bottom","10px solid white");

$(function () {
    
    $('.thumbnail').on('click', function () {
        //pridam sipku
        let thmbID = $(this).attr('id');
        $('.sipka').css("border-bottom","0px solid white");
        $('#'+thmbID+' .sipka').css("border-bottom","10px solid white");
        //zmenim obrazek ve wiev
        let imgSRC = $('#'+thmbID+' img').prop('src');
        $('#viewer').css("background","url('"+imgSRC+"')").css("background-size","960px 540px");
        //zmenim text
        $('#viewer .pictureDescription').hide()
        $('#'+thmbID+' .pictureDescription').clone().appendTo('#viewer')
    });



})