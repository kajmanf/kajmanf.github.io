let color = 'purple';
let number = 10;
let word = 'cool';

if (color = 'purple') {
    $(".N").css (
        'background' , 'purple'
    )
};

if (number > 100) {
    $(".E").text ("whoah, that's a big number")
} else {
    $(".E").text ("just a regular number, please.") 
}

if (word === 'cool') {
    $(".S").text ("Power of DOM")
} else {
    $(".W").text ("Power of DOM") 
}