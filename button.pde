class Button {
  PVector pos;
  color col;
  float h;
  float w;
  String label;
  int index;
  float blockHeight;
  boolean mouseIsOver;
  boolean selected;
  Button(PVector _pos, color _col, float _h, float _w, String _label, int _index, float _blockHeight) {
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
  void update(){
    
    mouseIsOver =  mouseOverButton();
    
  }
  void display() {
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
    text(label, (w/2)-(textWidth(label)*0.5), h-5);
    popMatrix();
  }
  boolean mouseOverButton() {
    boolean mouseOver = false;
    if (mouseX > pos.x && mouseX< pos.x + w && mouseY > pos.y && mouseY< pos.y + h ) {
      mouseOver = true;
    }
    return mouseOver;
  }
}

