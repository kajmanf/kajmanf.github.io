console.log('funguju')

$(function () {
    
    $('.button').on('click', function () {
        let obrazekID = $(this).attr('buttonid'); // obrazekID je nová proměnná; buttonid je (libovolný)atribut, který musím přidat v html
        $('#'+obrazekID).toggle(); // musím mít připravené html => id obrázků(div) musí odpovídat buttonid(atributu) tlačítka
        //alert(buttonid) => dostanu alert s atributem buttonid tlačítka
    
    });



})