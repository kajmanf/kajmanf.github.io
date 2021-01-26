console.log('Funguju')

$('#popUp').toggle()

$(function () {
    
    //tlacitko ZAVRIT
    $('#zavrit').on('click', function () {
        $('#popUp').toggle()
    });

    //tlaciko HODINA
    $('.hodina').on('click', function () {
        $('#popUp').toggle()
        let xyz = $(this).closest('div')

    //tlacitko rezervovat
        $('#rezervovat').on('click', function () {        
            let inicialy = $('#inicialy').val();
            let jmeno = $('#jmeno').val();
            let x = $('#inicialy').val().length;
            let y = $('#jmeno').val().length;
            console.log(x)
            console.log(y)
            if (x === 0 || y === 0) {
                alert('input chybí') 
            } else {
                let inicialyInput = '<p>'+inicialy+'</p>';
                $(inicialyInput).val().appendTo(xyz);
                $('#inPopUp')[0].reset();

            }
        }); 
    });




})