

Block [] blocks;
float sideLength = 0.25;
float iconSideLength = 15;
PVector iconPos;
ArrayList buttons;
ArrayList savedLayouts;
float numSteps = 10.0;
PFont font;
PFont heavyFont;
float multiplier = 80.0;
String fname = "saved_layouts.csv";
int gridSize = 20;
int halfGridSize = gridSize/2;
int setIndex = 0;
float setHeight=0;
boolean set = false;
float rot = PI*0.25;
float rotSpeed = 0.01;
boolean doRotate = false;
float buttonHeight = 20;
float buttonWidth = 150;

float buttonxShift = 350;
float buttonyShift = 30;
float loadButtonxShift;

void setup() {
  size(1200, 900, OPENGL);
  frame.setIconImage( getToolkit().getImage("sketch.ico") );
  blocks = new Block[gridSize*gridSize];
  smooth();
  float x=0;
  float y=0;
  iconPos = new PVector(50, 50, 0);
  heavyFont= loadFont("Avenir-Heavy-15.vlw");
  font = loadFont("Avenir-Light-14.vlw");
  textFont(font, 14);
  buttons  = new ArrayList();
  savedLayouts = new ArrayList();

  String [] labels = {
    "chair seat", "chair back", "stool", "sofa seat", "sofa arm", "sofa back", "table", "coffee table", "floor"
  };

  float [] heightPresets = {
    0.43, 0.7, 0.43, 0.43, 0.6, 0.8, 0.7, 0.45, 0.0
  };



  color buttonColor = color(#B2B2B2);
  for (int i =0;i<labels.length;i++) {
    //    if (i == labels.length -1) {
    //      buttonColor = color(0, 144, 100);
    //    }
    //    else {
    //      buttonColor = color(0, 244, 0);
    //    }
    Button abutton = new Button(new PVector(buttonxShift, buttonyShift +(i * buttonHeight) ), buttonColor, buttonHeight, buttonWidth, labels[i], i, heightPresets[i]);
    buttons.add(abutton);
  }

  String [] oldFile = loadStrings(fname);
  //  buttonColor = color(0, 144, 100);
  loadButtonxShift = width - (buttonWidth+10);
  for (int i =0;i<oldFile.length;i++) {
    String layoutName = splitTokens(oldFile[i], ",")[0];
    Button abutton = new Button(new PVector(loadButtonxShift, buttonyShift +(i * buttonHeight) ), buttonColor, buttonHeight, buttonWidth, layoutName, i, heightPresets[i]);
    savedLayouts.add(abutton);
  }

  float xShift = 10;
  float yShift = 10;

  for (int i=0;i<blocks.length;i++) {

    blocks[i] = new Block(new PVector(x*sideLength*multiplier, 0, y*sideLength*multiplier), new PVector(xShift+(x*iconSideLength), yShift+(y*iconSideLength), 0), color(#EFEFEF), 0, sideLength*multiplier, iconSideLength, i);

    x++;

    if (x>=gridSize) {
      x=0;
      y++;
    }
  }
  rectMode(CORNER);
}

void draw() {
  background(255);
  if (set) {
    //blocks[setIndex].setHeight(setHeight*multiplier,20.0);
    set=false;
  }
  // println(blocks[setIndex].isTweening  +"   "+  blocks[setIndex].tweenDirection);
  for (int i=0;i<blocks.length;i++) {
    blocks[i].update();
  }
  fill(0);
  text("New Selection",buttonxShift, buttonyShift-8);
  text("Saved Settings",loadButtonxShift, buttonyShift-8);
  pushMatrix();
  //translate(iconPos.x,iconPos.y,iconPos.z);
  pushStyle();
  for (int i=0;i<blocks.length;i++) {
    blocks[i].displayIcon();
  }
  popStyle();
  pushStyle();
  for (int i=0;i<buttons.size();i++) {
    Button aButton = (Button)buttons.get(i);
    aButton.update();
    aButton.display();
  }
  popStyle();
  pushStyle();
  for (int i=0;i<savedLayouts.size();i++) {
    Button aButton = (Button)savedLayouts.get(i);
    aButton.update();
    aButton.display();
  }
  popStyle();
  popMatrix();
  pushMatrix();
  translate(0, 0, 200);
  translate((width/2), (height/2), 0);
  rotateX(-0.25);
  rotateY(rot);

  translate(-(width/2), -(height/2), 0);

  translate((width/2)-(halfGridSize*sideLength*multiplier), (height/2)+50, -(halfGridSize*sideLength*multiplier));



  //rotateY(0.25*PI);
  if (doRotate) {
    rot +=rotSpeed;
  }
  pushStyle();
  for (int i=0;i<blocks.length;i++) {

    blocks[i].display();
  }
  popStyle();
  popMatrix();
}
float getGridLeftX(Block [] _blocks) {
  float leftX = width*2;
  for (int i=0;i<_blocks.length;i++) {
    if (_blocks[i].icon_pos.x<leftX) {
      leftX = _blocks[i].icon_pos.x;
    }
  }
  return leftX;
}
float getGridRightX(Block [] _blocks) {
  float rightX = 0;
  for (int i=0;i<_blocks.length;i++) {
    if (_blocks[i].icon_pos.x>rightX) {
      rightX = _blocks[i].icon_pos.x;
    }
  }
  return rightX+blocks[0].iconSideLength;
}

float getGridTopY(Block [] _blocks) {
  float topY = height*2;
  for (int i=0;i<_blocks.length;i++) {
    if (_blocks[i].icon_pos.y<topY) {
      topY = _blocks[i].icon_pos.y;
    }
  }
  return topY;
}
float getGridBottomY(Block [] _blocks) {
  float bottomY = 0;
  for (int i=0;i<_blocks.length;i++) {
    if (_blocks[i].icon_pos.y>bottomY) {
      bottomY = _blocks[i].icon_pos.y;
    }
  }
  return bottomY+blocks[0].iconSideLength;
}

void mouseReleased() {
  int index = 0;
  //change colour of grid
  //println(" right left bottom top " + getGridRightX(blocks) +" "+ getGridLeftX(blocks) +" "+ getGridBottomY(blocks) +" "+ getGridTopY(blocks) +" ");

  if ( mouseX <= getGridRightX(blocks) &&  mouseX >= getGridLeftX(blocks) && mouseY <= getGridBottomY(blocks) && mouseY >= getGridTopY(blocks)) {
    //println("relesaed in side");
    for (int i=0;i<blocks.length;i++) {
      //if ( mouseX > blocks[i].icon_pos.x && mouseX< blocks[i].icon_pos.x + (blocks[i].iconSideLength*multiplier) && mouseY > blocks[i].icon_pos.y && mouseY< blocks[i].icon_pos.y + (blocks[i].iconSideLength*multiplier)) {
      if (blocks[i].mouseOverIcon()) {
        blocks[i].selected = !blocks[i].selected;
      } 
      else {
        //blocks[i].selected=false;
      }
    }
  }
  else {
    //println("released elsewhere");
  }
  //
  for (int i=0;i<buttons.size();i++) {
    Button aButton = (Button)buttons.get(i);
    if ( aButton.mouseOverButton()) {
      // println(aButton.index);
      for (int j=0;j<blocks.length;j++) {
        if (blocks[j].selected) {

          blocks[j].setHeight(aButton.blockHeight*multiplier, 20.0);

          //   set = true;
          // setIndex = j;
          //setHeight =  aButton.blockHeight;
        }
      }
    } 
    else {
      // blocks[i].selected=false;
    }
  }
  for (int i=0;i<savedLayouts.size();i++) {
    println(i);
    Button aButton = (Button)savedLayouts.get(i);
    if ( aButton.mouseOverButton()) {
      
      for(int k=0;k<blocks.length;k++){
        blocks[k].selected =false;
      }
      loadSavedLayout(i, blocks);
      aButton.selected = true;
    } 
    else {
      aButton.selected = false;
    }
  }
}

void saveCurrentLayout( Block [] _blocks) {
  String [] oldFile = loadStrings(fname);
  PrintWriter output;
  output = createWriter("data/"+fname);
  for (int i = 0;i< oldFile.length;i++) {
    output.println(oldFile[i]);
  }
  String newLine = "layout_on_"+month()+"_"+day()+"_"+hour()+"_"+minute()+"_"+second();

  for (int i=0;i<_blocks.length;i++) {
    newLine+=",";
    newLine+=str(blocks[i].h);
  }
  output.println(newLine);
  output.flush();
  output.close();
}
void loadSavedLayout(int layout, Block [] _blocks) {
  String [] oldFile = loadStrings(fname);
  String [] heights = splitTokens(oldFile[layout], ",");
  if (_blocks.length == heights.length-1) {
    for (int i=0;i<_blocks.length;i++) {
      blocks[i].setHeight(float(heights[i+1]), numSteps);
    }
    println("blocks reset to "+heights[0]);
  }
  else {
    println("heights and number of blocks don't match "+heights.length+" "+_blocks.length);
  }
}
void keyPressed() {
  if (key=='s'||key=='S') {
    saveCurrentLayout(blocks);
  } 
  if (key=='r'||key=='R') {
    doRotate=!doRotate;
  } 
  if (key=='1') {
    //loadSavedLayout(0, blocks);
  }
}

void makeButtons(){
  
  
}
