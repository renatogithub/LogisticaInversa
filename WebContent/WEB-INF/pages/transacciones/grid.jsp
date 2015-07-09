<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <link rel="stylesheet" href="resources/css/slick.grid.css" type="text/css"/>
  <link rel="stylesheet" href="resources/css/jquery-ui-1.8.16.custom.css" type="text/css"/>
  <link rel="stylesheet" href="resources/css/examples.css" type="text/css"/>
  <link rel="stylesheet" href="resources/css/slick-default-theme.css" type="text/css"/>
  
 <style>
    .slick-cell.copied {
      background: blue;
      background: rgba(0, 0, 255, 0.2);
      -webkit-transition: 0.5s background;
    }
    
    
    /*
        weekcalendareditor
    */
    .wkclEdCont {
        width:210px;
        height:23px;
        overflow:hidden;
        margin-top:-1px;
        border:1px solid #DDD;
    }
    .wkclEdContNotEditing{
        width:210px;
        height:23px;
        overflow:hidden;
        margin-top:-1px;
        border-right:1px solid #DDD;
    }

    .wkclEdHalfDay{
        width:14px;
        height:100%;
        cursor:pointer;
        float:left;
    }
    .wkclEdDay{
        width:29px;
        height:100%;
        border-left:1px solid #DDD;
        float:left;
    }

    .wkclEdHeadCont{
        width:210px;
        height:23px;
        overflow:hidden;
        border:1px solid #FFF;
        border-bottom:none;
        margin-top:5px;
    }

    .wkclEdHeadDay{
        width:29px;
        height:100%;
        border-left:1px solid #DDD;
        float:left;
        text-align:center;
        margin-top:5px;
    }
    
  </style>
</head>
<body>
<div style="position:relative">
  <div style="width:600px;">
    <div id="myGrid" style="width:100%;height:500px;"></div>
  </div>
<!--   <div style="width:600px;">
    <div id="myGrid2" style="width:100%;height:500px;"></div>
  </div> -->

  <div class="options-panel">
    <h2>Excel compatible copy/paste manager</h2>
        <div>
          This is basically the same example than <a href="example-spreadsheet.html">example-spreadsheet.html</a>, with the support of external excel-compatible software clipboard<br />
          
        </div>
    <h2>Paste from Excel-compatible:</h2>
    <ul>
      <li>Copy a range of cells to clipboard in Excel</li>
      <li>Select a cell on slickgrid</li> 
      <li>Use Ctrl-V keyboard shortcut to paste from the clipboard</li> 
    </ul>
    <h2>Copy for Excel-compatible:</h2>
    <ul>
        <li>Select a range of cells with a mouse</li>
        <li>Use Ctrl-C shortcut to copy cells</li>
        <li>You can paste the tabular data into Excel</li>
    </ul>

    <h2>Undo/redo support :</h2>
    <ul>
      <li>Use buttons to undo/redo copy/paste</li>
    </ul>
    <button onclick="undoRedoBuffer.undo()"><img src="../images/arrow_undo.png" align="absmiddle"> Undo</button>
    <button onclick="undoRedoBuffer.redo()"><img src="../images/arrow_redo.png" align="absmiddle"> Redo</button>
  </div>
</div>

<script src="resources/js/firebugx.js"></script>
<script src="resources/js/jquery-1.7.min.js"></script>
<script src="resources/js/jquery-ui-1.8.16.custom.min.js"></script>
<script src="resources/js/jquery.event.drag-2.2.js"></script>
<script src="resources/js/slick.core.js"></script>
<script src="resources/plugins/slick.autotooltips.js"></script>
<script src="resources/plugins/slick.cellrangedecorator.js"></script>
<script src="resources/plugins/slick.cellrangeselector.js"></script>
<script src="resources/plugins/slick.cellexternalcopymanager.js"></script>
<script src="resources/plugins/slick.cellselectionmodel.js"></script>
<script src="resources/plugins/slick.editors.js"></script>
<script src="resources/plugins/slick.formatters.js"></script>
<script src="resources/plugins/slick.grid.js"></script>

<script>
  var grid;
  var data = [];
  var data2 = [];
  

  
  var options = {
    editable: true,
    enableAddRow: true,
    enableCellNavigation: true,
    asyncEditorLoading: false,
    autoEdit: false
  };



  var undoRedoBuffer = {
      commandQueue : [],
      commandCtr : 0,

      queueAndExecuteCommand : function(editCommand) {
        this.commandQueue[this.commandCtr] = editCommand;
        this.commandCtr++;
        editCommand.execute();
      },

      undo : function() {
        if (this.commandCtr == 0)
          return;

        this.commandCtr--;
        var command = this.commandQueue[this.commandCtr];

        if (command && Slick.GlobalEditorLock.cancelCurrentEdit()) {
          command.undo();
        }
      },
      redo : function() {
        if (this.commandCtr >= this.commandQueue.length)
          return;
        var command = this.commandQueue[this.commandCtr];
        this.commandCtr++;
        if (command && Slick.GlobalEditorLock.cancelCurrentEdit()) {
          command.execute();
        }
      }
  }

  // undo shortcut
  $(document).keydown(function(e)
  {
    if (e.which == 90 && (e.ctrlKey || e.metaKey)) {    // CTRL + (shift) + Z
      if (e.shiftKey){
        undoRedoBuffer.redo();
      } else {
        undoRedoBuffer.undo();
      }
    }
  });

  var pluginOptions = {
    clipboardCommandHandler: function(editCommand){ undoRedoBuffer.queueAndExecuteCommand.call(undoRedoBuffer,editCommand); },
    includeHeaderWhenCopying : false
  };

/*   var columns = [
                 {id: "title", name: "Title", field: "title"},
                 {id: "duration", name: "Duration", field: "duration"},
                 {id: "%", name: "% Complete", field: "percentComplete"},
                 {id: "start", name: "Start", field: "start"},
                 {id: "finish", name: "Finish", field: "finish"},
                 {id: "effort-driven", name: "Effort Driven", field: "effortDriven"}
               ]; */
  
   var columns = [
    {
      id: "selector",
      name: "Nro.",
      field: "num",
      width: 35
    }
  ]; 
   
               
//for(var i=0;i<20;i++){
	columns.push({
      id: 0,
      name: "Serie",
      field: i,
      width: 60,
      editor: Slick.Editors.Text
    }); 
	
	columns.push({
	      id: 1,
	      name: "Descripcion",
	      field: i,
	      width: 60,
	      editor: Slick.Editors.Text
	    }); 
	
	columns.push({
	      id: 1,
	      name: "Descripcion",
	      field: i,
	      width: 60,
	      editor: Slick.Editors.Text
	    }); 
	
	
	
//}               

//     for (var i = 0; i < 26; i++) {
//     /* 	 data[i] = {
//     		        title: "Task " + i,
//     		        duration: "5 days",
//     		        percentComplete: Math.round(Math.random() * 100),
//     		        start: "01/01/2009",
//     		        finish: "01/05/2009",
//     		        effortDriven: (i % 5 == 0) */
    		      
//      columns.push({
//       id: i,
//       //name: String.fromCharCode("A".charCodeAt(0) + i),
//       field: i,
//       width: 60,
//       editor: Slick.Editors.Text
//     }); 
//   }  
  
 // columns[4] = {id: "%", name: "% Complete", field: "percentComplete", width: 80, resizable: false, formatter: Slick.Formatters.PercentCompleteBar, editor: Slick.Editors.PercentComplete};
  //columns[5] = {id: "start", name: "Start", field: "start", minWidth: 60, editor: Slick.Editors.Date};
 

  $(function () {
    for (var i = 0; i < 100; i++) {
      var d = (data[i] = {});
      d["num"] = i;
      for (var j = 0; j < 26; j++) {
        d[j] = j+i;
      }
      d["percentComplete"] = Math.round(Math.random() * 100);
      d["start"] = new Date(Math.round(Math.random() * 1000000000));
      d["weekCalendar"] = [true, true, true, true, true, true, true, true, true, true, false, false, false, false];
    }
    
    grid = new Slick.Grid("#myGrid", data, columns, options);
    grid.setSelectionModel(new Slick.CellSelectionModel());
    grid.registerPlugin(new Slick.AutoTooltips());

    // set keyboard focus on the grid
    grid.getCanvasNode().focus();

    grid.registerPlugin(new Slick.CellExternalCopyManager(pluginOptions));
    

    grid.onAddNewRow.subscribe(function (e, args) {
      var item = args.item;
      var column = args.column;
      grid.invalidateRow(data.length);
      data.push(item);
      grid.updateRowCount();
      grid.render();
    });
    
   // grid2 = new Slick.Grid("#myGrid2", data, columns, options);
   // grid2.setSelectionModel(new Slick.CellSelectionModel());
    
   // grid2.registerPlugin(new Slick.CellExternalCopyManager(pluginOptions));
  })
</script>
</body>
</html>