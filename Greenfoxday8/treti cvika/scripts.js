console.log("JS is working!")

$(".button").click(function(){
    console.log("Yeah, you clicked me!");
});

$(".button2").click(function(){
    $(".button").text ("another");
});  

$(".button3").click(function(){
    $(".button2").css ("background", "blue"),
    $(".button3").css ("background", "red"),
    $(".button").css ("background", "yellow");
});  