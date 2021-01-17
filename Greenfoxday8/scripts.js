let currentYear = 2021;
let yearOfBirth = 1993;
let age = currentYear - yearOfBirth;
console.log(`I'm years ${age} old.`);

let warStart = 1938;
let warEnd = 1945;
let warLenght = warEnd - warStart;
console.log(`WW2 lasted ${warLenght} years.`);

let oneShare = 826.16;
let numberOfShares = 76;
let price = oneShare * numberOfShares;
console.log(`I'd need to pay ${price} $.`);

let hoursInDay = 24;
let minutesInHour = 60;
let secondsInMinute = 60;
let secondsOfDay = hoursInDay * minutesInHour * secondsInMinute;
console.log(`There's ${secondsOfDay} seconds in day.`);

let peopleInHungary = 9770000;
let peopleInWorld = 7800000000;
let hungarians = peopleInHungary / peopleInWorld * 100;
console.log(`Hungrary population is ${hungarians} % of the worlds.`);

let sizeOfCzech = 78866;
let sizeOfChina = 9597000;
let bigger = sizeOfChina / sizeOfCzech;
console.log(`China is ${bigger} square km larger.`);

let pocetMist = 7;
let pocetUcastniku = 654;
let pocetCelych = Math.floor(pocetUcastniku/pocetMist);
let participants = pocetUcastniku - pocetCelych * pocetMist;
console.log(`${participants} participants will be in last group.`);

let names = ["Adam", "Zdenek", "Ivetka", "Igor"];
let thirdName = names[2];
let Boolean = (thirdName.length > 5);
console.log(Boolean);

names.push("Jarmila");
console.log(names);

names[0] = "Filip";
console.log(names);

let secondBoolean = names.length > 4 ;
console.log(secondBoolean);

let objekt = {
    colors: ["Red","Green","Blue"]
};

objekt.hasManyColors = ["Yellow","Purple"];
console.log(objekt)

if (objekt['hasManyColors'].length >= 3)  {
    console.log("true");
  } else {
    console.log("false");    
  } ;

let highestNumber = 9;
for (let i = 0; i<=highestNumber; i++) {
  console.log(i)
};

console.log(" ")

let secondHighestNumber = highestNumber + 1;
for (let i = 0; i <= secondHighestNumber; i++) {
  console.log(i)
};

console.log(" ")

let max = 20;
for (let i = 2; i < max; i++) {
  if (i % 2 === 0) {
    console.log(i)
  }
};

console.log(" ")

let N = 10;
for (let i = 1; i <= N; i++) {
  console.log(i)
};

console.log(" ")


let greetingTo = jmeno => {
  console.log(`Hello ${jmeno}!`);
};

greetingTo("Adam"); // takto přidávám proměnné do funkce

console.log(" ")

let printValues = array => {
  for (let i = 0; i < array.length; i++) {
    console.log(array[i]);
  }
};

printValues ([0, 3, 6, 7, 9])

console.log(" ")

let myNumbers = [0, 3, 6, 7, 9];
myNumbers.forEach((number) => {
  console.log(number);
  })