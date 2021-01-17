//// VARIABLES / CONSTANTS ////

const initCodeRequest = `
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:gs="http://example.com/contrato">
  <soapenv:Header/>
  <soapenv:Body>
      <gs:getCountryRequest>
          <gs:name>Spain</gs:name>
      </gs:getCountryRequest>
  </soapenv:Body>
</soapenv:Envelope>
`.trim();

const codeRequestDOM = document.querySelector("#code-request");
const codeResponseDOM = document.querySelector("#code-response");

const editorRequest = CodeMirror.fromTextArea(codeRequestDOM, {
  lineNumbers: true,
  mode: "text/html",
  viewportMargin: Infinity
});

const editorResponse = CodeMirror.fromTextArea(codeResponseDOM, {
  lineNumbers: true,
  mode: "text/html",
  readOnly: "nocursor", //ou true
  viewportMargin: Infinity
});


//// INITIALIZE ////

// highlight.js
hljs.initHighlightingOnLoad();
//hljs.initLineNumbersOnLoad();

editorRequest.setValue(initCodeRequest);
editorRequest.save();

document.querySelector("#form-request-body").addEventListener("submit", function (event) {
  event.preventDefault()
  const body = codeRequestDOM.value;
  sendSoapRequest(body);
});

//// FUNCTIONS ////

async function sendSoapRequest(data) {

  const setReponseInDOM = function (request) {
    const requestURIDOM = document.querySelector("#request-uri");
    const requestHeadersDOM = document.querySelector("#request-headers");
    const responseHeadersDOM = document.querySelector("#response-headers");
    
    requestURIDOM.innerText = `${request.config.method.toUpperCase()} ${request.config.url}`;
    requestHeadersDOM.innerText = JSON.stringify(request.config.headers);
    responseHeadersDOM.innerText = JSON.stringify(request.headers);
  };

  const config = {
    method: 'post',
    url: '/ws',
    headers: { 
      // 'Content-Type': 'application/soap+xml; charset=utf-8'
      'Content-Type': 'text/xml'
    },
    data : data
  };

  try {
    const response = await axios(config);
    editorResponse.setValue(formatXml(response.data));
    setReponseInDOM(response);
    editorResponse.save()
  } catch (error) {
    if (!error.response) {
      console.error('Network Error')
      return;
    }

    const errorCode = error.response.status
    const errorMessage = error.response.data.message || error.message
    editorResponse.setValue(`${errorCode} - ${errorMessage}`)
    editorResponse.save()
    setReponseInDOM(error.response);
  }

  document.querySelector('#container-soap-request').scrollIntoView();
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
