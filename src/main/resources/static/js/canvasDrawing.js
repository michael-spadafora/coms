// <canvas id="canvas" width="300" height="300"></canvas>
// import {fabric} from 'fabric';
var canvas = this.__canvas = new fabric.Canvas('canvas', {
    isDrawingMode: true
    // backgroundColor : "white"
});



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

// function addTextbox() {
//     var x = document.getElementById("fonts").value;
//     canvas.add(new fabric.IText('this is my text box', {
//         // width: 150,
//         top: 50,
//         left: 50,
//         fontSize: 60,
//         textAlign: 'center',
//         // fontFamily: 'Arial'
//         fontFamily: document.getElementById("fonts").value
//         // fixedWidth: 150
//     }));
// }

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

function clearPane() {
    canvas.clear();
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
//
// function deleteGroup() {
//   console.log("delete group");
//   console.log(canvas.getActiveObject());
//   console.log(typeof(canvas.getActiveObject()));
//   // console.log(canvas.getActiveObject().length);
//   // var objects = canvas.getActiveObject();
//   // canvas.discardActiveObject();
//   //
//   //   canvas.remove(objects);
//   var group = canvas.getActiveObject().getObjects();
//   console.log(group);
//   for (let i in group){
//     // console.log(group[i]);
//     canvas.remove(group[i])
//   }
// }

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

// function saveToBack() {


//     var file = canvas.toJSON();
//     var json = {
//         fabricJSON: JSON.stringify(file),
//         image: canvas.toDataURL()
//     }
//     console.log(json);
//     fetch('savePanel', {
//         method: 'post',
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         body: json
//     });

//     // var imgObj = new Image();
//     // imgObj.src = dataUrl;
//     // imgObj.onload = function(event) {
//     //     var image = new fabric.Image(imgObj);
//     //     image.width = image.width;
//     //     image.height = image.height;
//     //     image.scale(1050/image.getScaledWidth());
//     //     // console.log(image);
//     //     canvas.add(image);
//     // }
//     // .then(res => res.json())
//     // .then(grid => reset_grid(grid));
// }

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
        canvas.requestRenderAll();
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

function saveimaging(){
    var dataUrl=canvas.toDataURL();
    fetch('comic/createComic', {
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dataUrl)
    });
    console.log(JSON.stringify(dataUrl));
    // canvas.getElement().toBlob(temp);
    // console.log(pics);
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
  fabric.loadSVGFromURL('./img/chat.svg', function(objects, options) {
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
  fabric.loadSVGFromURL('./img/thought.svg', function(objects, options) {
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
  fabric.loadSVGFromURL('./img/arrow.svg', function(objects, options) {
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
  fabric.loadSVGFromURL('./img/scream.svg', function(objects, options) {
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
  fabric.loadSVGFromURL('./img/action.svg', function(objects, options) {
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


// canvas.on('mouse:down', function(options) {
//   console.log('canvas event');
//   var keyCode = event.which || event.keyCode || 0;
//   console.log(keyCode);
// });


var canvasWrapper = document.getElementById('mydiv');
canvasWrapper.tabIndex = 1000;
canvasWrapper.addEventListener("keydown", myfunc);
function myfunc(e){
  // console.log("key is pressed");
  // console.log(e.keyCode);
  if (e.keyCode == 46){
    deleteObject();
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





















// var objectsFromUndoRedo = 0; 
// canvas.on(
//     'object:modified', function () {
//     updateModifications(true);
// });
// canvas.on(
//     'object:added', function () {
//         if (objectsFromUndoRedo > 0){
//             objectsFromUndoRedo -= 1;
//         }else{
//             updateModifications(true);
//         }
//     // updateModifications(true);
// });

// function updateModifications(changes) {
//     if (changes === true) {
//         myjson = JSON.stringify(canvas);
//         undoStack.push(myjson);
//         redoStack = [];
//     }
//     //mods = 0;
// }


// function undo(){
//     if (undoStack.length > 0){
//         canvas.clear().renderAll();
//         redoStack.push(undoStack.pop());
//         objectsFromUndoRedo = JSON.parse(undoStack[undoStack.length - 1])["objects"].length
//         canvas.loadFromJSON(undoStack[undoStack.length - 1]);
//         canvas.renderAll();
//     }
// }

// function redo(){
//     if (redoStack.length > 0){
//         canvas.clear().renderAll();
//         objectsFromUndoRedo = JSON.parse(redoStack[redoStack.length - 1])["objects"].length
//         canvas.loadFromJSON(redoStack[redoStack.length - 1]);
//         undoStack.push(redoStack.pop());
//         canvas.renderAll();
//     }
// }












// undo/redo for changing colors should only happen when a change occurs (nothing when erorr occurs)
//check the previous state. if previous and current are the same, then no push, else push

// undo/redo for lines

// var canvas = new fabric.Canvas('c');
// canvas.isDrawingMode = true;
// canvas.on('object:added',function(){
//   if(!isRedoing){
//     h = [];
//   }
//   isRedoing = false;
// });

// var isRedoing = false;
// var h = [];
// function undo(){
//   if(canvas._objects.length>0){
//    h.push(canvas._objects.pop());
//    canvas.renderAll();
//   }
// }
// function redo(){
  
//   if(h.length>0){
//     isRedoing = true;
//    canvas.add(h.pop());
//   }
// }





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


