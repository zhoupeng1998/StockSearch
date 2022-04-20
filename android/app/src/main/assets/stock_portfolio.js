
$('#shares').html(PortfolioInterface.getQuantity());
$('#avgcost').html(PortfolioInterface.getAvgCost());
$('#total').html(PortfolioInterface.getTotalCost());
$('#change').html(PortfolioInterface.getChange());
$('#value').html(PortfolioInterface.getMarketValue());

if (Number(PortfolioInterface.getChange()) < 0) {
    $('#changetd').css('color', 'red');
    $('#valuetd').css('color', 'red');
} else if (Number(PortfolioInterface.getChange()) > 0) {
    $('#changetd').css('color', 'green');
    $('#valuetd').css('color', 'green');
}