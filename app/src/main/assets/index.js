var url = location.search.substring(1);

PDFJS.cMapUrl = 'https://unpkg.com/pdfjs-dist@1.10.100/cmaps/';
PDFJS.cMapPacked = true;

var pdfDoc = null;

function createPage() {
    var div = document.createElement("canvas");
    document.body.appendChild(div);
    return div;
}
//function getFromAndroid(str){
// 		//document.getElementById("android").innerHTML=str;
// 		alert(str);
//}

function renderPage(num) {
    pdfDoc.getPage(num).then(function (page) {
        var viewport = page.getViewport(3);
        var canvas = createPage();
        var ctx = canvas.getContext('2d');

        canvas.height = viewport.height;
        canvas.width = viewport.width;

        page.render({
            canvasContext: ctx,
            viewport: viewport
        });
    });
}
 PDFJS.workerSrc = './pdf.worker.js'

PDFJS.getDocument(url).then(function (pdf) {
    pdfDoc = pdf;
    for (var i = 1; i <= pdfDoc.numPages; i++) {
        renderPage(i)
        if(i == pdfDoc.numPages){
            window.injs.runOnAndroidJavaScript("from JavaScript",1);//调用android的函数
        }
    }
}).catch(function (pdf){
    window.injs.runOnAndroidJavaScript("from JavaScript",2);//调用android的函数
});