import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class grid_furniture extends PApplet {



Block [] blocks;
float sideLength = 0.25f;
float iconSideLength = 15;
PVector iconPos;
ArrayList buttons;
ArrayList savedLayouts;
float numSteps = 10.0f;
PFont font;
PFont heavyFont;
float multiplier = 80.0f;
String fname = "saved_layouts.csv";
int gridSize = 20;
int halfGridSize = gridSize/2;
int setIndex = 0;
float setHeight=0;
boolean set = false;
float rot = PI*0.25f;
float rotSpeed = 0.01f;
boolean doRotate = false;
float buttonHeight = 20;
float buttonWidth = 150;

float buttonxShift = 350;
float buttonyShift = 30;
float loadButtonxShift;

public void setup() {
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
    0.43f, 0.7f, 0.43f, 0.43f, 0.6f, 0.8f, 0.7f, 0.45f, 0.0f
  };



  int buttonColor = color(0xffB2B2B2);
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

    blocks[i] = new Block(new PVector(x*sideLength*multiplier, 0, y*sideLength*multiplier), new PVector(xShift+(x*iconSideLength), yShift+(y*iconSideLength), 0), color(0xffEFEFEF), 0, sideLength*multiplier, iconSideLength, i);

    x++;

    if (x>=gridSize) {
      x=0;
      y++;
    }
  }
  rectMode(CORNER);
}

public void draw() {
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
  rotateX(-0.25f);
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
public float getGridLeftX(Block [] _blocks) {
  float leftX = width*2;
  for (int i=0;i<_blocks.length;i++) {
    if (_blocks[i].icon_pos.x<leftX) {
      leftX = _blocks[i].icon_pos.x;
    }
  }
  return leftX;
}
public float getGridRightX(Block [] _blocks) {
  float rightX = 0;
  for (int i=0;i<_blocks.length;i++) {
    if (_blocks[i].icon_pos.x>rightX) {
      rightX = _blocks[i].icon_pos.x;
    }
  }
  return rightX+blocks[0].iconSideLength;
}

public float getGridTopY(Block [] _blocks) {
  float topY = height*2;
  for (int i=0;i<_blocks.length;i++) {
    if (_blocks[i].icon_pos.y<topY) {
      topY = _blocks[i].icon_pos.y;
    }
  }
  return topY;
}
public float getGridBottomY(Block [] _blocks) {
  float bottomY = 0;
  for (int i=0;i<_blocks.length;i++) {
    if (_blocks[i].icon_pos.y>bottomY) {
      bottomY = _blocks[i].icon_pos.y;
    }
  }
  return bottomY+blocks[0].iconSideLength;
}

public void mouseReleased() {
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

          blocks[j].setHeight(aButton.blockHeight*multiplier, 20.0f);

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

public void saveCurrentLayout( Block [] _blocks) {
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
public void loadSavedLayout(int layout, Block [] _blocks) {
  String [] oldFile = loadStrings(fname);
  String [] heights = splitTokens(oldFile[layout], ",");
  if (_blocks.length == heights.length-1) {
    for (int i=0;i<_blocks.length;i++) {
      blocks[i].setHeight(PApplet.parseFloat(heights[i+1]), numSteps);
    }
    println("blocks reset to "+heights[0]);
  }
  else {
    println("heights and number of blocks don't match "+heights.length+" "+_blocks.length);
  }
}
public void keyPressed() {
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

public void makeButtons(){
  
  
}
class Block {
  PVector pos;
  PVector icon_pos;
  int col;
  float h;
  float sideLength;
  float iconSideLength;
  boolean selected ;
  boolean isTweening;
  float targetHeight;
  float tweenSpeed;
  int tweenDirection =0;
  boolean mouseIsOver;
  boolean hasBeenSelected = false;
  int index;
  Block(PVector _pos, PVector _icon_pos, int _col, float _h, float _sideLength, float _iconSideLength, int _index) {
    pos=_pos;
    icon_pos = _icon_pos;
    col = _col;
    h = _h;
    sideLength = _sideLength;
    iconSideLength=_iconSideLength;
    selected = false;
    isTweening = false;
    mouseIsOver = false;
    index = _index;
  } 
  public void update() {
    mouseIsOver = mouseOverIcon();
    if (h!=0){
      hasBeenSelected  = true;
    }
    else{
      hasBeenSelected  = false;
    }
    if (isTweening) {

      if (tweenDirection == -1) {

        //println("tweening large target"+frameCount);
        if ( h < targetHeight) {
          //println(h+" tweening");
          h+=(tweenSpeed);
        }
        else {
          
          h=targetHeight;
          isTweening = false;
          if (h!=0) {
            //println(index +" is done tweening. Height is : "+h/80.0);
          }
        }
      }
      else if (tweenDirection == 1) {
        // println("tweening small target"+frameCount);

        if ( h > targetHeight) {
          h-=(tweenSpeed);
        }
        else {
          
          h=targetHeight;
          isTweening = false;
          if (h!=0) {
            //println(index +" is done tweening. Height is : "+h/80.0);
          }
        }
      }
    }
    else {
      //println("not tweenings");
    }
  }
  public void display() {
    pushMatrix();
    // pushStyle();
    fill(col);

    translate(pos.x, pos.y -(0.5f*h), pos.z);
    box(sideLength, h, sideLength);
    // popStyle();
    popMatrix();
    //box(35);
  }
  public void displayIcon() {

    pushMatrix();
    if (!selected) {
      if (mouseIsOver) {
        fill(100);
      }
      else {
        if (hasBeenSelected) {
          fill(80);
        }
        else {
          fill(0xffD6D6D5);
        }
      }
    }
    else if (selected) {
      fill(0, 0, 255);
    }
    translate(icon_pos.x, icon_pos.y, icon_pos.z);
    rect(0, 0, iconSideLength, iconSideLength);
    // popStyle();
    popMatrix();
    //box(35);
  }
  public boolean mouseOverIcon() {
    boolean isOver= false;
    if (mouseX>icon_pos.x && mouseX < icon_pos.x+ iconSideLength && mouseY>icon_pos.y && mouseY < icon_pos.y+ iconSideLength) {
      isOver = true;
    }
    return isOver;
  }
  //tweenspeed is number of steps
  public void setHeight(float _h, float _tweenSpeed) {
    //println("set tween to "+_h);
    isTweening = true;
    //tweenSpeed = _tweenSpeed;

    //println(tweenSpeed);
    targetHeight=_h;
    tweenSpeed =abs ((h-targetHeight)/_tweenSpeed);
    if (targetHeight>h) {
      tweenDirection = -1;
    }
    else {
      tweenDirection = 1;
    }
    println("h "+h+ " tweenDirection "+tweenDirection+" targetHeight "+targetHeight+" tweenSpeed "+tweenSpeed+" _tweenSpeed "+_tweenSpeed);
  }
}

class Button {
  PVector pos;
  int col;
  float h;
  float w;
  String label;
  int index;
  float blockHeight;
  boolean mouseIsOver;
  boolean selected;
  Button(PVector _pos, int _col, float _h, float _w, String _label, int _index, float _blockHeight) {
    pos=_pos;
    col = _col;
    h = _h;
    w = _w;
    label = _label;
    index = _index;
    blockHeight =_blockHeight;
    mouseIsOver = false;
    selected = false;
  } 
  public void update(){
    
    mouseIsOver =  mouseOverButton();
    
  }
  public void display() {
    pushMatrix();
    // pushStyle();
    if(mouseIsOver){
      fill(100);
    }
    else{
      fill(col);
    }
    translate(pos.x, pos.y);
    rect (0, 0, w, h);
    fill(0);
    if(selected){
      println("selected");
     textFont(heavyFont, 15); 
    }
    else{
     textFont(font, 14); 
    }
    textMode(CENTER);
    text(label, (w/2)-(textWidth(label)*0.5f), h-5);
    popMatrix();
  }
  public boolean mouseOverButton() {
    boolean mouseOver = false;
    if (mouseX > pos.x && mouseX< pos.x + w && mouseY > pos.y && mouseY< pos.y + h ) {
      mouseOver = true;
    }
    return mouseOver;
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "grid_furniture" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
