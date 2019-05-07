// <canvas id="canvas" width="300" height="300"></canvas>
// import {fabric} from 'fabric';
var canvas = new fabric.Canvas('canvas', {
    isDrawingMode: false
    // backgroundColor : "white"
});

canvas.setDimensions({width: 1050, height: 350});



var $ = function(id) {
    return document.getElementById(id)
};

var undoStack = [];
undoStack.push(JSON.stringify(canvas));
var redoStack = [];
var redo_undo_status = false;


fabric.Object.prototype.transparentCorners = false;

var drawingModeEl = $('drawing-mode'),
    drawingOptionsEl = $('drawing-mode-options'),
    drawingLineWidthEl = $('drawing-line-width'),
    drawingColorEl = $('drawing-color'),
    drawingShadowColorEl = $('drawing-shadow-color'),
    drawingShadowWidth = $('drawing-shadow-width'),
    drawingShadowOffset = $('drawing-shadow-offset'),
    clearEl = $('clear-canvas');

clearEl.onclick = function() {
    canvas.clear();
    updateModifications(true);

};

drawingModeEl.onclick = function() {
    canvas.isDrawingMode = !canvas.isDrawingMode;
    if (canvas.isDrawingMode) {
        drawingModeEl.innerHTML = 'Exit drawing';
        // drawingOptionsEl.style.display = '';
    } else {
        drawingModeEl.innerHTML = 'Enter drawing';
        // drawingOptionsEl.style.display = 'none';
    }
};

if (fabric.PatternBrush) {
    var vLinePatternBrush = new fabric.PatternBrush(canvas);
    vLinePatternBrush.getPatternSrc = function() {

        var patternCanvas = fabric.document.createElement('canvas');
        patternCanvas.width = patternCanvas.height = 10;
        var ctx = patternCanvas.getContext('2d');

        ctx.strokeStyle = this.color;
        ctx.lineWidth = 5;
        ctx.beginPath();
        ctx.moveTo(0, 5);
        ctx.lineTo(10, 5);
        ctx.closePath();
        ctx.stroke();

        return patternCanvas;
    };

    var hLinePatternBrush = new fabric.PatternBrush(canvas);
    hLinePatternBrush.getPatternSrc = function() {

        var patternCanvas = fabric.document.createElement('canvas');
        patternCanvas.width = patternCanvas.height = 10;
        var ctx = patternCanvas.getContext('2d');

        ctx.strokeStyle = this.color;
        ctx.lineWidth = 5;
        ctx.beginPath();
        ctx.moveTo(5, 0);
        ctx.lineTo(5, 10);
        ctx.closePath();
        ctx.stroke();   

        return patternCanvas;
    };

    var squarePatternBrush = new fabric.PatternBrush(canvas);
    squarePatternBrush.getPatternSrc = function() {

        var squareWidth = 10,
            squareDistance = 2;

        var patternCanvas = fabric.document.createElement('canvas');
        patternCanvas.width = patternCanvas.height = squareWidth + squareDistance;
        var ctx = patternCanvas.getContext('2d');

        ctx.fillStyle = this.color;
        ctx.fillRect(0, 0, squareWidth, squareWidth);

        return patternCanvas;
    };

    var diamondPatternBrush = new fabric.PatternBrush(canvas);
    diamondPatternBrush.getPatternSrc = function() {

        var squareWidth = 10,
            squareDistance = 5;
        var patternCanvas = fabric.document.createElement('canvas');
        var rect = new fabric.Rect({
            width: squareWidth,
            height: squareWidth,
            angle: 45,
            fill: this.color
        });

        var canvasWidth = rect.getBoundingRect().width;

        patternCanvas.width = patternCanvas.height = canvasWidth + squareDistance;
        rect.set({
            left: canvasWidth / 2,
            top: canvasWidth / 2
        });

        var ctx = patternCanvas.getContext('2d');
        rect.render(ctx);

        return patternCanvas;
    };

}

$('drawing-mode-selector').onchange = function() {

    if (this.value === 'hline') {
        canvas.freeDrawingBrush = vLinePatternBrush;
    } else if (this.value === 'vline') {
        canvas.freeDrawingBrush = hLinePatternBrush;
    } else if (this.value === 'square') {
        canvas.freeDrawingBrush = squarePatternBrush;
    } else if (this.value === 'diamond') {
        canvas.freeDrawingBrush = diamondPatternBrush;
    } else if (this.value === 'Poka-dots') {
        canvas.freeDrawingBrush = new fabric["PatternBrush"](canvas);
    } else {
        canvas.freeDrawingBrush = new fabric[this.value + 'Brush'](canvas);
    }

    if (canvas.freeDrawingBrush) {
        canvas.freeDrawingBrush.color = drawingColorEl.value;
        canvas.freeDrawingBrush.width = parseInt(drawingLineWidthEl.value, 10) || 1;
        canvas.freeDrawingBrush.shadow = new fabric.Shadow({
            blur: parseInt(drawingShadowWidth.value, 10) || 0,
            offsetX: 0,
            offsetY: 0,
            affectStroke: true,
            color: drawingShadowColorEl.value,
        });
    }
};

drawingColorEl.onchange = function() {
    canvas.freeDrawingBrush.color = this.value;
};
drawingShadowColorEl.onchange = function() {
    canvas.freeDrawingBrush.shadow.color = this.value;
};
drawingLineWidthEl.onchange = function() {
    canvas.freeDrawingBrush.width = parseInt(this.value, 10) || 1;
    this.previousSibling.innerHTML = this.value;
};
drawingShadowWidth.onchange = function() {
    canvas.freeDrawingBrush.shadow.blur = parseInt(this.value, 10) || 0;
    this.previousSibling.innerHTML = this.value;
};
drawingShadowOffset.onchange = function() {
    canvas.freeDrawingBrush.shadow.offsetX = parseInt(this.value, 10) || 0;
    canvas.freeDrawingBrush.shadow.offsetY = parseInt(this.value, 10) || 0;
    this.previousSibling.innerHTML = this.value;
};

if (canvas.freeDrawingBrush) {
    canvas.freeDrawingBrush.color = drawingColorEl.value;
    canvas.freeDrawingBrush.width = parseInt(drawingLineWidthEl.value, 10) || 1;
    canvas.freeDrawingBrush.shadow = new fabric.Shadow({
        blur: parseInt(drawingShadowWidth.value, 10) || 0,
        offsetX: 0,
        offsetY: 0,
        affectStroke: true,
        color: drawingShadowColorEl.value,
    });
};

function drawRec() {
    canvas.add(new fabric.Rect({
        top: 0,
        left: 0,
        width: 100,
        height: 50,
        stroke: 'black',
        strokeWidth: 1,
        fill: 'rgba(0,0,0,0)',
        backgroundColor: 'rgba(0,0,0,0)'
    }));
    canvas.renderAll();
    console.log("this is a rectangle");
// updateModifications(true);
}

function drawLine() {
    canvas.add(new fabric.Line([0, 50, 50, 50], {
        stroke: 'black',
        strokeWidth: 10
    }));
// updateModifications(true);
}

function drawTriangle() {
    canvas.add(new fabric.Triangle({
        top: 0,
        left: 0,
        stroke: 'black',
        strokeWidth: 1,
        fill: 'rgba(0,0,0,0)',
        backgroundColor: 'rgba(0,0,0,0)'
    }));
// updateModifications(true);
}

function drawSquare() {
    canvas.add(new fabric.Rect({
        top: 0,
        left: 0,
        width: 100,
        height: 100,
        stroke: 'black',
        strokeWidth: 1,
        fill: 'rgba(0,0,0,0)',
        backgroundColor: 'rgba(0,0,0,0)'
    }));
// updateModifications(true);
}

function drawCircle() {
    canvas.add(new fabric.Circle({
        radius: 10,
        left: 0,
        top: 0,
        stroke: 'black',
        strokeWidth: 1,
        fill: 'rgba(0,0,0,0)',
        backgroundColor: 'rgba(0,0,0,0)'
    }));
// updateModifications(true);
}


function addTextbox() {
    var x = document.getElementById("fonts").value;
    canvas.add(new fabric.IText('this is my text box', {
        // width: 150,
        top: 50,
        left: 50,
        fontSize: document.getElementById("font-size").value,
        textAlign: 'center',
        // fontFamily: 'Arial'
        fontFamily: document.getElementById("fonts").value
        // fixedWidth: 150
    }));
// updateModifications(true);
}


function deleteObject() {
    // console.log(canvas.getActiveObject());
    // if (canvas.getActiveObject().length){
    //   console.log("true");
    // }else {
    //   console.log("false");
    // }
    // console.log("size of selected");
    // console.log(canvas.getActiveObject().length);
    // var count = 0;
    var grouping = canvas.getActiveObject();
    // for (let p in grouping){
    //   count++;
    // }
    console.log(grouping);
    console.log("counting ended");

    var group = canvas.getActiveObject();
    canvas.remove(group);
    group = canvas.getActiveObject();
    console.log(group);
    if (!(group == null)){
        console.log("not null");
        var group = canvas.getActiveObject().getObjects();
        console.log(group);
        for (i in group){
            // console.log(group[i]);
            canvas.remove(group[i])
        }
        canvas.discardActiveObject();
    }else {
        console.log("is null");
    }

updateModifications(true);
}

function changeStroke() {
    var x = document.getElementById("stroke").value;
    console.log(x);
    canvas.getActiveObject().set("stroke", x);
    canvas.renderAll();
updateModifications(true);
}

function changeFill() {
    var x = document.getElementById("fill").value;
    // console.log(canvas.getActiveObject().get("fill"))
    if (canvas.getActiveObject().get("fill") == x){
        console.log("same color")
    }else{
        console.log("color change")
        canvas.getActiveObject().set("fill", x);
        canvas.renderAll();
        updateModifications(true);
    }

}

function removeFill() {
    if (canvas.getActiveObject().get("fill") == 'rgba(0,0,0,0)'){
        console.log("same color")
    }else{
        console.log("color change")
        canvas.getActiveObject().set("fill", 'rgba(0,0,0,0)');
        canvas.renderAll();
        updateModifications(true);
    }

}


function copy() {
    canvas.getActiveObject().clone(function(cloned) {
        _clipboard = cloned;
    });
}

function cut() {
    canvas.getActiveObject().clone(function(cloned) {
        _clipboard = cloned;
    });
    deleteObject();
    // canvas.remove(canvas.getActiveObject());

// updateModifications(true);
}

function paste() {
    _clipboard.clone(function(clonedObj) {
        canvas.discardActiveObject();
        clonedObj.set({
            left: clonedObj.left + 10,
            top: clonedObj.top + 10,
            evented: true,
        });
        if (clonedObj.type === 'activeSelection') {
            // active selection needs a reference to the canvas.
            clonedObj.canvas = canvas;
            clonedObj.forEachObject(function(obj) {
                canvas.add(obj);
            });
            // this should solve the unselectability
            clonedObj.setCoords();
        } else {
            canvas.add(clonedObj);
        }
        _clipboard.top += 10;
        _clipboard.left += 10;
        canvas.setActiveObject(clonedObj);
        canvas.renderAll();
    });
// updateModifications(true);
}

document.getElementById('imgLoader').onchange = function handleImage(e) {
    var reader = new FileReader();
    reader.onload = function(event) {
        console.log('fdsf');
        var imgObj = new Image();
        imgObj.src = event.target.result;
        console.log(imgObj.src);
        // console.log(imgObj.height);
        console.log(imgObj);
        imgObj.onload = function(event) {
            var image = new fabric.Image(imgObj);
            image.set({
                left: 0,
                top: 0,
                padding: 10,
                cornersize: 10
            });
            if (image.width > 350 && (image.width >= image.height)){
              image.scale(350/image.getScaledWidth());
            }
            if (image.height > 1000 && (image.height >= image.width)){
              image.scale(1050/image.getScaledHeight());
            }
            canvas.add(image);
            // updateModifications(true);
        }

    }
    reader.readAsDataURL(e.target.files[0]);
    document.getElementById("imgLoader").value = "";
}


function downloadImage(){
    var x = document.getElementById("canvas");
    console.log(x);
    console.log(x.get);
    canvas.getElement().toBlob(function(blob){
        saveAs(blob, "myCanvasImage.png")
    });
    // var picsx = canvas.getElement().toBlob(function(blob){
    //   saveAs(blob, "myCanvas.png")
    // });
    // console.log(pics);
    console.log("hello world");
}


function encode( s ) {
    var out = [];
    for ( var i = 0; i < s.length; i++ ) {
        out[i] = s.charCodeAt(i);
    }
    return new Uint8Array( out );
}

function downloadJSON() {
  var y = canvas.toJSON();
  // console.log(y);
  // canvas.loadFromJSON(y)
    // var data = encode(JSON.stringify(y));
    var data = JSON.stringify(y);

    var blob = new Blob( [ data ], {
        type: 'application/octet-stream'
    });

    url = URL.createObjectURL( blob );
    var link = document.createElement( 'a' );
    link.setAttribute( 'href', url );
    link.setAttribute( 'download', 'example.json' );

    var event = document.createEvent( 'MouseEvents' );
    event.initMouseEvent( 'click', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, null);
    link.dispatchEvent( event );
}



function sendToBack() {
  var activeObject = canvas.getActiveObject();
    if (activeObject) {
      canvas.sendToBack(activeObject);
    }
updateModifications(true);
}

function back() {
  var activeObject = canvas.getActiveObject();
    if (activeObject) {
      canvas.sendBackwards(activeObject);
    }
updateModifications(true);
}
function front() {
  var activeObject = canvas.getActiveObject();
    if (activeObject) {
      canvas.bringForward(activeObject);
    }
updateModifications(true);
}

function bringToFront() {
  var activeObject = canvas.getActiveObject();
    if (activeObject) {
      canvas.bringToFront(activeObject);
    }
updateModifications(true);
}

function chat() {
  console.log('adding chat svg');
  fabric.loadSVGFromURL('/img/chat.svg', function(objects, options) {
    var loadedObject = fabric.util.groupSVGElements(objects, options);
    loadedObject.set({
      left: 0,
      top: 0
    })
    .setCoords();
    loadedObject.scaleToWidth(100);
    canvas.add(loadedObject);
  });
// updateModifications(true);
}
function thought() {
  console.log('adding chat svg');
  fabric.loadSVGFromURL('/img/thought.svg', function(objects, options) {
    var loadedObject = fabric.util.groupSVGElements(objects, options);
    loadedObject.set({
      left: 0,
      top: 0
    })
    .setCoords();
    loadedObject.scaleToWidth(100);
    canvas.add(loadedObject);
  });
// updateModifications(true);
}
function arrow() {
  console.log('adding chat svg');
  fabric.loadSVGFromURL('/img/arrow.svg', function(objects, options) {
    var loadedObject = fabric.util.groupSVGElements(objects, options);
    loadedObject.set({
      left: 0,
      top: 0
    })
    .setCoords();
    loadedObject.scaleToWidth(100);
    canvas.add(loadedObject);
  });
// updateModifications(true);
}
function scream() {
  console.log('adding chat svg');
  fabric.loadSVGFromURL('/img/scream.svg', function(objects, options) {
    var loadedObject = fabric.util.groupSVGElements(objects, options);
    loadedObject.set({
      left: 0,
      top: 0
    })
    .setCoords();
    loadedObject.scaleToWidth(100);
    canvas.add(loadedObject);
  });
// updateModifications(true);

}

function action() {
  console.log('adding chat svg');
  fabric.loadSVGFromURL('/img/action.svg', function(objects, options) {
    var loadedObject = fabric.util.groupSVGElements(objects, options);
    loadedObject.set({
      left: 0,
      top: 0
    })
    .setCoords();
    loadedObject.set("stroke", "#000000");
    loadedObject.set("fill", "#ffffff");
    loadedObject.scaleToWidth(100);
    canvas.add(loadedObject);
  });
// updateModifications(true);

}



var canvasWrapper = document.getElementById('mydiv');
canvasWrapper.tabIndex = 1000;
canvasWrapper.addEventListener("keydown", keydown);
canvasWrapper.addEventListener("keyup", keyup);
var control = false;
var shift = false;

function keydown(e) {
    // console.log("key is pressed");
    // console.log(e.keyCode);
    console.log("")
    
    if (e.keyCode == 46) {
        deleteObject();
        console.log("delete");
    }
    if (e.keyCode == 17){
        control = true;
        console.log("control key is pressed");
    }
    if (e.keyCode == 16){
        shift = true;
        console.log("shift key is pressed");
    }
    if (control && e.keyCode == 67) {
        console.log("copy keys");
        copy();
    }
    if (control && e.keyCode == 86) {
        console.log("paste keys");
        paste();
    }
    if (control && e.keyCode == 88) {
        console.log("cut keys");
        cut();
    }
    if (shift && control && e.keyCode == 90) {
        console.log("redo keys");
        redo();
    }
    else if (control && e.keyCode == 90) {
        console.log("undo keys");
        undo();
    }
}
function keyup(e){
    if (e.keyCode == 17){
        control = false;
        console.log("control key is let go");
    }
    if (e.keyCode == 16){
        shift = false;
        console.log("shift key is let go");
    }
    
}



document.getElementById('jsonLoader').onchange = function handleImage(e) {
    var reader = new FileReader();
    console.log("hello world");
    reader.onload = function(event) {
      var json;
        console.log('fdsf');
        var imgObj = new Image();
        imgObj.src = event.target.result;
        console.log(imgObj.src);
        console.log("---------");
        var request = new XMLHttpRequest();
        request.open('GET', imgObj.src);
        request.responseType = 'json';
        request.send();
        request.onload = function() {
          var oldJson = request.response;
          console.log(oldJson);
          console.log(oldJson["objects"]);
          var old = oldJson["objects"];
          var current = canvas.toJSON()["objects"];
          var newcanvas = current.concat(old);
          json = {"objects": newcanvas}
          redo_undo_status = true;
          canvas.loadFromJSON(json, canvas.renderAll.bind(canvas), function(o, object) {
          fabric.log(o, object);
      });
      redo_undo_status = false;
      updateModifications(true);
        }


    }
    reader.readAsDataURL(e.target.files[0]);
    document.getElementById("jsonLoader").value = "";


}



canvas.on(
    'object:modified', function () {
    updateModifications(true);
});
canvas.on(
    'object:added', function () {
      if (redo_undo_status == false){
        updateModifications(true);
      }
//     updateModifications(true);
});

function updateModifications(changes) {
    if (changes === true) {
        myjson = JSON.stringify(canvas);
        undoStack.push(myjson);
        redoStack = [];
    }
    //mods = 0;
}


function undo(){
    if (undoStack.length > 1){
        canvas.clear().renderAll();
        redoStack.push(undoStack.pop());
        redo_undo_status = true;
        canvas.loadFromJSON(undoStack[undoStack.length - 1]);
        redo_undo_status = false;
        canvas.renderAll();
    }
}

function redo(){
    if (redoStack.length > 0){
        canvas.clear().renderAll();
        redo_undo_status = true;
        canvas.loadFromJSON(redoStack[redoStack.length - 1]);
        redo_undo_status = false;
        undoStack.push(redoStack.pop());
        canvas.renderAll();
    }
}


function loading(oldCanvas){
    redo_undo_status = true;
    canvas.loadFromJSON(oldCanvas, function() {
        canvas.renderAll();
    },function(o,object){
        console.log(o,object)
    });
    undoStack[0] = JSON.stringify(canvas);
    redo_undo_status = false;

}


document.getElementById('backgroundImage').addEventListener("change", function(e) {
   var file = e.target.files[0];
   var reader = new FileReader();
   reader.onload = function(f) {
      var data = f.target.result;
      fabric.Image.fromURL(data, function(img) {
         // add background image
         canvas.setBackgroundImage(img, canvas.renderAll.bind(canvas), {
            scaleX: canvas.width / img.width,
            scaleY: canvas.height / img.height
         });
         updateModifications(true);
      });
   };
   reader.readAsDataURL(file);
});

function removeBackground() {
  console.log(canvas.backgroundImage);
  if (canvas.backgroundImage != null){
    canvas.setBackgroundImage(null, canvas.renderAll.bind(canvas));
     updateModifications(true);
     document.getElementById("backgroundImage").value = "";
  }

}

function save() {
    console.log(canvas.toJSON());
    console.log(canvas.toDataURL);
    var requestBody = {
        "body": JSON.stringify(canvas.toJSON()),
        "image": canvas.toDataURL()
    }
    // $.ajax({
    //     type: 'POST',
    //     url: "/savePanel",
    //     data: requestBody,
    //     success: [function(data) {
    //         console.log(data);
    //         //$("body").html(data);
    //     }],
    //     error: function(exception) {
    //         alert('Exeption:' + exception);
    //     }
    // });

    fetch('/savePanel', {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json', 
        },
        body: JSON.stringify(requestBody)
    }).then(res => res.json()).then(res => console.log(res)).catch(error => alert('Exeption:' + error));
    

}


function group() {
    if (!canvas.getActiveObject()) {
      return;
    }
    if (canvas.getActiveObject().type !== 'activeSelection') {
      return;
    }
    canvas.getActiveObject().toGroup();
    canvas.renderAll();
  }

 function ungroup() {
    if (!canvas.getActiveObject()) {
      return;
    }
    if (canvas.getActiveObject().type !== 'group') {
      return;
    }
    canvas.getActiveObject().toActiveSelection();
    canvas.renderAll();
  }

  canvas.on('mouse:wheel', function(opt) {
    var delta = opt.e.deltaY;
    var pointer = canvas.getPointer(opt.e);
    var zoom = canvas.getZoom();
    zoom = zoom + delta/500;
    if (zoom > 20) zoom = 20;
    if (zoom < 1) zoom = 1;
    canvas.zoomToPoint({ x: opt.e.offsetX, y: opt.e.offsetY }, zoom);
    opt.e.preventDefault();
    opt.e.stopPropagation();
    readjustCanvasBounds();
  });
  canvas.on('mouse:down', function(opt) {
    var evt = opt.e;
    if (evt.altKey === true) {
      this.isDragging = true;
      this.selection = false;
      this.lastPosX = evt.clientX;
      this.lastPosY = evt.clientY;
    }
  });
  canvas.on('mouse:move', function(opt) {
    if (this.isDragging) {
        var e = opt.e;
        this.viewportTransform[4] += e.clientX - this.lastPosX;
        this.viewportTransform[5] += e.clientY - this.lastPosY;
        this.lastPosX = e.clientX;
        this.lastPosY = e.clientY;
    }
});
  canvas.on('mouse:up', function(opt) {
    this.isDragging = false;
    this.selection = true;
    readjustCanvasBounds();
    // var zero = canvas.viewportTransform[0];
    // var one = canvas.viewportTransform[1];
    // var two = canvas.viewportTransform[2];
    // var three = canvas.viewportTransform[3];
    // var four = canvas.viewportTransform[4];
    // var five = canvas.viewportTransform[5];
    // if (this.viewportTransform[4] > 0) {
    //     four = 0;
    // }
    // if (this.viewportTransform[5] > 0) {
    //     five = 0;
    // }
    // if (four/one - 1050 < -1050) {
    //     four = -(1050*zero - 1050)
    // }

    // if (five/zero-350<-350){
    //     console.log("too low");
    //     five = -(350*zero - 350);
    // }
    // canvas.setViewportTransform([zero, one, two, three, four, five]);
    // this.renderAll();
  });

  function readjustCanvasBounds(){
    var zero = canvas.viewportTransform[0];
    var one = canvas.viewportTransform[1];
    var two = canvas.viewportTransform[2];
    var three = canvas.viewportTransform[3];
    var four = canvas.viewportTransform[4];
    var five = canvas.viewportTransform[5];
    if (canvas.viewportTransform[4] > 0) {
        four = 0;
    }
    if (canvas.viewportTransform[5] > 0) {
        five = 0;
    }
    if (four < -(1050*zero - 1050)) {
        four = -(1050*zero - 1050)
    }

    if (five < -(350*zero - 350)){
        console.log("too low");
        five = -(350*zero - 350);
    }
    canvas.setViewportTransform([zero, one, two, three, four, five]);
    canvas.renderAll();
  }

  canvas.on("object:moving", function(e){
    var obj = e.target;
    obj.setCoords();

    var bound = obj.getBoundingRect(true);
    var width = obj.canvas.width;
    var height = obj.canvas.height;

    obj.left = Math.min(Math.max(0, bound.left), width - bound.width);
    obj.top = Math.min(Math.max(0, bound.top), height - bound.height);
})

function zoomin(){
	var zoom = canvas.getZoom();
  canvas.setZoom(zoom*1.2);
}
function zoomout(){
    var zoom = canvas.getZoom();
    if (zoom/1.2 < 1){
        canvas.setZoom(1);
    }else{
        canvas.setZoom(zoom/1.2);
    }
}
function recenter(){
    canvas.setViewportTransform([1, 0, 0, 1, 0, 0]);
    canvas.renderAll();
    
}