class Block {
  PVector pos;
  PVector icon_pos;
  color col;
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
  Block(PVector _pos, PVector _icon_pos, color _col, float _h, float _sideLength, float _iconSideLength, int _index) {
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
  void update() {
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
  void display() {
    pushMatrix();
    // pushStyle();
    fill(col);

    translate(pos.x, pos.y -(0.5*h), pos.z);
    box(sideLength, h, sideLength);
    // popStyle();
    popMatrix();
    //box(35);
  }
  void displayIcon() {

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
          fill(#D6D6D5);
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
  boolean mouseOverIcon() {
    boolean isOver= false;
    if (mouseX>icon_pos.x && mouseX < icon_pos.x+ iconSideLength && mouseY>icon_pos.y && mouseY < icon_pos.y+ iconSideLength) {
      isOver = true;
    }
    return isOver;
  }
  //tweenspeed is number of steps
  void setHeight(float _h, float _tweenSpeed) {
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

