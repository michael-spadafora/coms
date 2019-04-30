// <canvas id="canvas" width="300" height="300"></canvas>
// import {fabric} from 'fabric';
var canvas = this.__canvas = new fabric.Canvas('canvas', {
    isDrawingMode: true
});

var $ = function(id) {
    return document.getElementById(id)
};


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
    canvas.clear()
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
}

function drawLine() {
    canvas.add(new fabric.Line([0, 50, 50, 50], {
        stroke: 'black',
        strokeWidth: 10
    }));
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
}

function clearPane() {
    canvas.clear();
}

function deleteObject() {
    // console.log(canvas.getActiveObject());
    // if (canvas.getActiveObject().length){
    //   console.log("true");
    // }else {
    //   console.log("false");
    // }
    console.log("size of selected");
    console.log(canvas.getActiveObject().length);
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
    canvas.getActiveObject().set("stroke", x);
    canvas.renderAll();
}

function changeFill() {
    var x = document.getElementById("fill").value;
    canvas.getActiveObject().set("fill", x);
    canvas.renderAll();
}

function removeFill() {
    canvas.getActiveObject().set("fill", 'rgba(0,0,0,0)');
    canvas.renderAll();
}

function saveToBack() {


    var file = canvas.toJSON();
    var json = {
        fabricJSON: JSON.stringify(file),
        image: canvas.toDataURL()
    }
    console.log(json);
    fetch('savePanel', {
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        body: json
    });

    // var imgObj = new Image();
    // imgObj.src = dataUrl;
    // imgObj.onload = function(event) {
    //     var image = new fabric.Image(imgObj);
    //     image.width = image.width;
    //     image.height = image.height;
    //     image.scale(1050/image.getScaledWidth());
    //     // console.log(image);
    //     canvas.add(image);
    // }
    // .then(res => res.json())
    // .then(grid => reset_grid(grid));
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
}

document.getElementById('imgLoader').onchange = function handleImage(e) {
    var reader = new FileReader();
    reader.onload = function(event) {
        console.log('fdsf');
        var imgObj = new Image();
        imgObj.src = event.target.result;
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
            image.width = image.width;
            image.height = image.height;
            image.scale(500/image.getScaledWidth());
            console.log(image);
            canvas.add(image);
        }

    }
    reader.readAsDataURL(e.target.files[0]);
    document.getElementById("imgLoader").value = "";
}

function saveimaging(){
    var dataUrl=canvas.toDataURL();
    // console.log(dataUrl);
    // var imgObj = new Image();
    // imgObj.src = dataUrl;
    // imgObj.onload = function(event) {
    //     var image = new fabric.Image(imgObj);
    //     image.set({
    //         left: 0,
    //         top: 0,
    //         padding: 10,
    //         cornersize: 10
    //     });
    //     image.width = image.width;
    //     image.height = image.height;
    //     image.scale(1050/image.getScaledWidth());
    //     // console.log(image);
    //     canvas.add(image);
    // }
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
        saveAs(blob, "myCanvas.png")
    });
    // var picsx = canvas.getElement().toBlob(function(blob){
    //   saveAs(blob, "myCanvas.png")
    // });
    // console.log(pics);
}


//nothing