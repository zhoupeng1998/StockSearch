$('#openPrice').html(InfoInterface.getOpenPrice());
$('#highPrice').html(InfoInterface.getHighPrice());
$('#lowPrice').html(InfoInterface.getLowPrice());
$('#prevClosePrice').html(InfoInterface.getPrevClosePrice());
$('#ipo').html(InfoInterface.getIpo());
$('#industry').html(InfoInterface.getIndustry());
$('#webPage').html(InfoInterface.getWebPage());

var peers_str = "";
var peers_raw = InfoInterface.getPeers();
var peers_data = JSON.parse(peers_raw);
if (peers_data.length > 0) {
    peers_str += `<a href="javascript:void(0)" onclick="gotoWebPage()">${peers_data[0]}<\a>`
    //`${ticker} Hourly Price Variation`
}
for (var i = 1; i < peers_data.length; i++) {
    peers_str += `, <a href="javascript:void(0)" onclick="gotoPeersActivity('${peers_data[i]}')">${peers_data[i]}<\a>`
}

$('#peers').html(peers_str);

function gotoWebPage() {
    InfoInterface.gotoWebPage();
}

function gotoPeersActivity(ticker) {
    InfoInterface.gotoPeersActivity(ticker);
}