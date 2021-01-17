

// $(selector).action()
//takto vypadá stavba v jQuery

$('div').css('background', 'purple')

$('button').click(() => {
    $('.first').toggleClass('hidden');
})
// takto se přidává funkce tlačítku //

// před jQuery

console.log("JavaScript is working!!!"); 
// odeslání textu do konzoly//

let age = 30;
let colors = ["orange", "blue", "green", "purple"];
// takto se přidává hodnota proměnným//

if (age > 18) {
    console.log("You're an adult");
}   
//takto se píší podmínky//

if (age >= 18) {
    console.log("You're an adult");
} else {
    console.log("You're a kid");    
}  
//takto se píší podmínky s true/false//

for (let i = 0; i < 10; i++) {
    console.log(i);
}
// takto se tvoří smyčka ... pokračuje, dokud je výsledek TRUE //    
// 1.i =  proměnná //
// 2.i =  podmínka //
// 3.i =  co se stane //

let posts = [{
    title: "First post",
    likeCount: 12
},  {
    title: "Second post",
    likeCount: 222
}];

console.log(colors);

colors.forEach(color => {
    console.log(color)
});
// vypíše jednu barvu po druhé. Slovně=> pro každou hodnotu v bloku colors spáhám příkaz console log //


let myFunction = x => {
    console.log(x)
;}
// let myFunction = x => x * x //
// vytvoření funkce , podobně jako f(x)= x*x//


let greeter = name => {
    console.log(`Hello, ${name}`);
    if (name === "Adam") {
        console.log("he's an adult");
    }
};

greeter("Adam")
greeter("Erik")
greeter("Peter")
greeter("Reka")
// vytvoření smyčky u proměnnés podmínkou

console.log ("This is the end of log!")
//