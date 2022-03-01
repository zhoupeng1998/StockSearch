let validateNews = feed => {
    if (feed.datetime == undefined || feed.datetime == 0) {
        return false;
    }
    if (feed.headline == undefined || feed.headline.length == 0) {
        return false;
    }
    if (feed.image == undefined || feed.image.length == 0) {
        return false;
    }
    if (feed.url == undefined || feed.url.length == 0) {
        return false;
    }
    return true;
}

let filternews = data => {
    var num = 0;
    var result = [];
    for (i = 0; num < 20 && i < data.length; i++) {
        if (validateNews(data[i])) {
            result.push(data[i]);
            num++;
        }
    }
    return result;
}

module.exports = filternews