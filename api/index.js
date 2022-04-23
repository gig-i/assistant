const express = require("express");
var app = express();
const CC = require('currency-converter-lt')
let currencyConverter = new CC()


const responses = require("./responses.json");
app.set('view engine', 'pug')
currencyConverter.from("USD").to("TRY").amount(1).convert().then((response) => {




const trigger = [
 
["merhaba", "naber", "nasılsın", "ne haber", "hey"],

["dolar", "türk lirası"],

["teşekkürler", "sağolasın", "sağol", "teşekkür"],


["sence"]
];


const reply =  [
//0 
["İyiyim sen?", "İyi gidiyor!", "Sesiniz kadar harika!", "Süper ötesi", "Hey selam!"], 

[
 response

],

[
    "Rica ederim", "Önemli değil"
  ],

["Evet tabii ki", "Hayır tabii ki", "Kesinlikle evet", "Kesinlikle hayır", "Evimi arabamı ortaya koyarım ki evet", "Evimi arabamı ortaya koyarım ki hayır"]
];







const alternative = [
  "Ne dediğini anlayamadım.",
  "Şuanlık bunun ne olduğunu bilmiyorum.",
  "Üzgünüm yardımcı olamıyorum",
  "Tam olarak anlamadım"
];



function output(input) {
  let product;
  let text = input;

  text = text
    .replace(/ a /g, " ")
    .replace(/i feel /g, "")
    .replace(/whats/g, "what is")
    .replace(/please /g, "")
    .replace(/ please/g, "");



  if (compare(trigger, reply, text)) {
    product = compare(trigger, reply, text);
  } else if (text.match(/robot/gi)) {
    product = robot[Math.floor(Math.random() * robot.length)];
  } else {
    product = alternative[Math.floor(Math.random() * alternative.length)];
  }


  return product;
}



function compare(triggerArray, replyArray, text) {
  let item;
  for (let x = 0; x < triggerArray.length; x++) {
    for (let y = 0; y < replyArray.length; y++) {
      console.log(text);
      if (text.includes(triggerArray[x][y])) {
        items = replyArray[x];
        item = items[Math.floor(Math.random() * items.length)];
      }
    }
  }
  return item;
}


app.get("/", (req,res)=>{
  res.render('index.pug')
})


app.get("/api/chat", (req,res)=>{

    if (req.query.msg === null || req.query.msg === undefined) return res.json({'response': 'Ne dediğini anlayamadım'});
  
  let speechMsg = req.query.msg.toLowerCase();
  console.log(speechMsg);
  res.json({'response':  output(speechMsg) });
});


app.listen(8080);
})