console.log('hello world')

        $('#t1 .sipka').css("border-bottom","10px solid white");
        //zmenim obrazek ve wiev
        let imgSRC = $('#t1 img').prop('src');
        $('#viewer').css("background","url('"+imgSRC+"')").css("background-size","960px 540px");
        //zmenim text
        $('#viewer .pictureDescription').hide()
        $('#t1 .pictureDescription').clone().appendTo('#viewer')


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

    $('#sipka2').on('click', function () {
        //let wievID = $('#viewer .thumbnail').attr('id');  
        let viewID = thmbID
        alert(viewID)  
    });
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*let x = 2;
    $('#sipka2').on('click', function () {
        /*
        if (hour < 18) {
            greeting = "Good day";
          } else {
            greeting = "Good evening";
          }
        

        let a = x++;
        let thmbID2 = 't'+a;
        

        //pridam sipku
        $('.sipka').css("border-bottom","0px solid white");
        $('#'+thmbID2+' .sipka').css("border-bottom","10px solid white");
        //zmenim obrazek ve wiev
        let imgSRC = $('#'+thmbID2+' img').prop('src');
        $('#viewer').css("background","url('"+imgSRC+"')").css("background-size","960px 540px");
        //zmenim text
        $('#viewer .pictureDescription').hide()
        $('#'+thmbID2+' .pictureDescription').clone().appendTo('#viewer')
    }); */

})