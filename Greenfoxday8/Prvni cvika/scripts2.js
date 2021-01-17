console.log(" ");

console.log("Hello world!");

console.log(" ");

$("#fox-1").text(   //změním text podle id v <p> //
    'Macrotis'
);

$("#fox-2").css(   //změním barvu ohraničení id v <p> //
    'border', '5px solid purple'  // !!!POZOR!!! musím rozdělit zvlášť, né jako v css border: 5px ...
);

$("p").css(   //změním barvu ohraničení id v <p> //
    'background', 'yellow'  // !!!POZOR!!! musím rozdělit zvlášť, né jako v css border: 5px ...
);
