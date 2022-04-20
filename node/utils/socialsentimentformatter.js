let formatSocialSentiment = data => {
    var res = {
        symbol: data.symbol, 
        reddit: {
            mention: 0,
            positiveMention: 0,
            negativeMention: 0
        }, 
        twitter: {
            mention: 0,
            positiveMention: 0,
            negativeMention: 0
        }};
    for (var i = 0; i < data.reddit.length; i++) {
        res.reddit.mention += data.reddit[i].mention;
        res.reddit.positiveMention += data.reddit[i].positiveMention;
        res.reddit.negativeMention += data.reddit[i].negativeMention;
    }
    for (var j = 0; j < data.twitter.length; j++) {
        res.twitter.mention += data.twitter[j].mention;
        res.twitter.positiveMention += data.twitter[j].positiveMention;
        res.twitter.negativeMention += data.twitter[j].negativeMention;
    }
    return res;
};

module.exports = formatSocialSentiment;