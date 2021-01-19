console.log("JS funguje!")


let N = 0;
$(".increase").click(function(){
    N++;
    $("h2").text(N);
}); 

$(".decrease").click(function(){
    let C = $('h2').val();
    console.log(C);
    /*let A = $(this).
    $("h2").text(A+1);
    */
}); 


