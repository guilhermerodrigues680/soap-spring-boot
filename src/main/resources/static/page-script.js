const codeBodyDOM = document.querySelector("#code");

const editor = CodeMirror.fromTextArea(codeBodyDOM, {
  lineNumbers: true,
  theme: "monokai",
  mode: "text/html"
});


const formBody = document.querySelector("#form-body");

formBody.addEventListener("submit", function (event) {
  event.preventDefault()
  const body = codeBodyDOM.value;
  console.log(body)
  
  sendSoapRequest(body);
});

////////

function sendSoapRequest(data) {
  const config = {
    method: 'post',
    url: 'http://localhost:8080/ws',
    headers: { 
      // 'Content-Type': 'application/soap+xml; charset=utf-8'
      'Content-Type': 'text/xml'
    },
    data : data
  };

  axios(config)
  .then(function (response) {
    console.log(response);
    console.log(formatXml(response.data))
  })
  .catch(function (error) {
    console.log(error);
  });
}

function formatXml(xml) {
  let formatted = '';
  const reg = /(>)(<)(\/*)/g;
  xml = xml.replace(reg, '$1\r\n$2$3');
  let pad = 0;


  xml.split('\r\n').forEach(function (node) {
    let indent = 0;
    if (node.match(/.+<\/\w[^>]*>$/)) {
      indent = 0;
    } else if (node.match(/^<\/\w/)) {
      if (pad != 0) {
        pad -= 1;
      }
    } else if (node.match(/^<\w[^>]*[^\/]>.*$/)) {
      indent = 1;
    } else {
      indent = 0;
    }

    let padding = '';
    for (let i = 0; i < pad; i++) {
      padding += '  ';
    }

    formatted += padding + node + '\r\n';
    pad += indent;
  });

  return formatted;
}
