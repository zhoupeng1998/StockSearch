from flask import Flask, request, jsonify
import requests
import json
from datetime import datetime
from dateutil.relativedelta import relativedelta
import pytz

key = 'c7qen62ad3i9it666nrg'
app = Flask(__name__)

monthDict = {
    1:  'January',
    2:  'Febuary',
    3:  'March',
    4:  'April',
    5:  'May',
    6:  'June',
    7:  'July',
    8:  'August',
    9:  'September',
    10: 'October',
    11: 'November',
    12: 'December'
}

def formatTime(timestamp):
    date = datetime.fromtimestamp(timestamp)
    return str(date.day) + " " + monthDict[date.month] + " " + str(date.year)
    
def validateNews(feed):
    #if feed["category"] == None or len(feed["category"]) == 0:
        #return False
    if feed["datetime"] == None or feed["datetime"] == 0:
        return False
    if feed["headline"] == None or len(feed["headline"]) == 0:
        return False
    #if feed["id"] == None or feed["id"] == 0:
        #return False
    if feed["image"] == None or len(feed["image"]) == 0:
        return False
    #if feed["related"] == None or len(feed["related"]) == 0:
        #return False
    #if feed["source"] == None or len(feed["source"]) == 0:
        #return False
    #if feed["summary"] == None or len(feed["summary"]) == 0:
        #return False
    if feed["url"] == None or len(feed["url"]) == 0:
        return False
    return True

@app.route("/", methods=['GET'])
@app.route("/index", methods=['GET'])
def indexPage():
    return app.send_static_file("index.html")

@app.route("/<name>")
def helloPage(name):
    return "hello world, " + name

@app.route("/profile/<name>", methods=['GET'])
def getProfile(name):
    func = request.args.get('callback')
    payload = {'symbol': name.upper(), 'token': key}
    r = requests.get("https://finnhub.io/api/v1/stock/profile2", params=payload)
    raw = r.json()
    if 'ticker' in raw.keys():
        raw['found'] = 'Y'
    else:
        raw['found'] = 'N'
    if type(func) == str:
        print(raw)
        return '{funcname}({data})'.format(
            funcname=func,
            data=jsonify(raw),
        )
    else:
        return jsonify(raw)

@app.route("/summary/<name>", methods=['GET'])
def getSummary(name):
    func = request.args.get('callback')
    payload = {'symbol': name.upper(), 'token': key}
    rQuote = requests.get("https://finnhub.io/api/v1/quote", params=payload)
    rRecom = requests.get("https://finnhub.io/api/v1/stock/recommendation", params=payload)
    rawQuote = rQuote.json()
    rawRecom = rRecom.json()
    rawQuote["t"] = formatTime(rawQuote["t"])
    msgRecom = {}
    if len(rawRecom) > 0:
        msgRecom = rawRecom[0]
    result = {'symbol': name.upper(), 'quote': rawQuote, 'recommendation': msgRecom}
    if type(func) == str:
        return '{funcname}({data})'.format(
            funcname=func,
            data=jsonify(result),
        )
    else:
        return jsonify(result)

@app.route("/candle/<name>", methods=['GET'])
def getCandle(name):
    func = request.args.get('callback')
    enddate = datetime.now(pytz.timezone('America/Los_Angeles'))
    startdate = enddate + relativedelta(months=-6, days=-1)
    payload = {'symbol': name.upper(), 'resolution': 'D' ,'token': key, 
        'from': int(datetime.timestamp(startdate)), 'to': int(datetime.timestamp(enddate))}
    r = requests.get("https://finnhub.io/api/v1/stock/candle", params=payload)
    raw = r.json()
    if raw['s'] == 'ok':
        del raw['h']
        del raw['l']
        del raw['o']
        raw['t'] = [t * 1000 for t in raw['t']] 
    raw['today'] = enddate.strftime('%Y-%m-%d')
    if type(func) == str:
        return '{funcname}({data})'.format(
            funcname=func,
            data=jsonify(raw),
        )
    else:
        return jsonify(raw)


@app.route("/news/<name>", methods=['GET'])
def getNews(name):
    func = request.args.get('callback')
    enddate = datetime.now(pytz.timezone('America/Los_Angeles'))
    startdate = enddate + relativedelta(days=-30)
    payload = {'symbol': name.upper(), 'token': key, 
        'from': startdate.strftime('%Y-%m-%d'), 'to': enddate.strftime('%Y-%m-%d')}
    r = requests.get("https://finnhub.io/api/v1/company-news", params=payload)
    raw = r.json()
    print(len(raw))
    selected = []
    i = 0
    while i < len(raw) and len(selected) < 5:
        if validateNews(raw[i]):
            raw[i]["datetime"] = formatTime(raw[i]["datetime"])
            selected.append(raw[i])
        i += 1
    if type(func) == str:
        return '{funcname}({data})'.format(
            funcname=func,
            data=jsonify(selected),
        )
    else:
        return jsonify(selected)

# run the app.
if __name__ == "__main__":
    # Setting debug to True enables debug output. This line should be
    # removed before deploying a production app.
    app.debug = True
    app.run()