package com.mrajlabs.interspaceinvaders.invaders3;

import processing.core.*;
import android.util.DisplayMetrics;

// Processing library reference manual: https://processing.org/reference/

public class invaders3 extends PApplet {

Bubble b;
Tarbit t;
Score s;
Health h;
Power p;
Time ti;

// The game is initialized at level 0 with health bar at 100 and power bar at 0
int level = 0;
int ot = 0;
int to = 0;
int startTime;

float sd;
float dd; 


public void setup() {
  orientation(PORTRAIT); 

  // Getting display metrics in order to scale to different phone's dpi
  DisplayMetrics metrics = new DisplayMetrics();
  getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
  sd = metrics.densityDpi;

  // 1 pixel on a 160dpi screen is the standard so dividing the density by 160 gives the scale
  // Anywhere the pixel size is multiplied by dd, it means it is being scaled for the exact phone size

  dd = sd/160; 


  // millis() is a built in processing function that counts milliseconds
  startTime = millis();

  b = new Bubble();
  t = new Tarbit();
  s = new Score();
  h = new Health();
  p = new Power();
  ti = new Time();
}

public void draw() {

  // Sets up the background in pink (RGB value)
  background(254, 69, 152);

  // Update highscore 
  if (s.score >= s.highscore) {
    s.highscore = s.score;
  }

  // Increase the yspeed of the falling tarbits by 1 pixel every 15 seconds 
  if (ti.currentTime==15) {
    t.yspeed = t.yspeed+1*dd;
    ti.reset();
    startTime = millis();
  }

  // Game states
  if (level == 0) {
    introLevel();
  } else if (level == 1) {
    level1();
    ot = 0;
  } else if (level == 2) {
    level2();
    ot = 25;
  } else if (level == 3) {
    level3();
    ot = 50;
  } else if (level == 4) {
    level4();
    ot = 75;
  } else if (level == -1) {
    levelLose();
  } else if (level == -2) {
    instructions(); 
  }
}

// Game mode Level 0 (Main menu/intro screen)
public void introLevel() {
  float rx, ry;
  float rw=150*dd;
  float rh=130*dd;
  float ex, ey;
  float ew=200*dd;
  float eh=200*dd;
  float cx, cy;
  float cw=180*dd;
  float ch=360*dd;
  float bx, by;
  float bw = 10*dd;
  float bh = 200*dd;
  float ix, iy;
  float iw = 70*dd;
  float ih = 70*dd;
  background(254, 69, 152);

  // Drawing the body
  rectMode(CENTER);
  fill(0);
  rect(cx=displayWidth/2, cy=470*dd, cw, ch, 20*dd);
  
  // Drawing the lung as 1 rounded rectangle
  fill (255);
  noStroke();
  rectMode(CORNER);
  rect(rx=displayWidth/2-rw/2, ry=320*dd, rw, rh, 40*dd);

  // Drawing the instruction box
  strokeWeight(3*dd);
  stroke(0);
  rect(ix = 0, iy = 0, iw, ih, 20*dd);
  noStroke();
  fill(0);
  textSize(60*dd);
  text("?", 35*dd, 55*dd);

  // Drawing the black line separating the lungs
  rectMode(CENTER);
  rect(bx=displayWidth/2, by=420*dd, bw, bh);

  // Drawing the head
  ellipse(ex=displayWidth/2, ey=160*dd, ew, eh);

  // Writing the coming soon text
  fill(255);
  textAlign(CENTER);
  textSize(15*dd);
  text("LIVER & PANCREAS\nGAMES COMING SOON!", width/2, displayHeight/1.3f);

  // If the user clicks within the lung rectangle coordinates, the level variable = 1 and the game will begin
  if (mouseX>=rx && mouseX <=rx+rw && mouseY>=ry && mouseY <=ry+rh) {
    if (mousePressed) {
      level = 1;
      ti.reset();
      startTime = millis();
    }
  }

  // If the user clicks within the instruction box coordinates, the level = -2 and the instructions appear 
  if (mouseX>=ix && mouseX <=ix+iw && mouseY>=iy && mouseY <=iy+ih) {
    if (mousePressed) {
      level = -2;
    }
  }
}

// Game mode Level -2 (instruction screen)
public void instructions() {

    // Setting background to black and text to pink
    background(0);
    fill(254, 69, 152);
    textAlign(CENTER,CENTER);
    textSize(23*dd);
    String in1 = "Welcome to INTERSPACE INVADERS!";

    // Putting the string in1 into a textbox
    text(in1, displayWidth/2, 53*dd);

    // Text colour to white and writing large string of text for the instructions
    fill(255);
    textSize(20*dd);
    String in2 = "Click the lungs to begin!\n\nPlay as an alveolar macrophage and clean the lungs from cigarette tar.\nCatch the falling tar bits by sliding ⟵left & right⟶\n\nEvery miss, you lose 25% health:\n100% = Stage 0 COPD;\n75%  = Stage 1 COPD;\n50%  = Stage 2 COPD;\n25%  = Stage 3 COPD;\n0%    = Stage 4 COPD;\n\nYour immune system weakens as you lose health but the blue power bar can save you!\n\nThe power bar represents cessation of smoking. The more damage to your health, the more cessation power needed to reverse the effects of COPD! Stage 4 COPD is irreversible: GAME OVER!";
    
    // Putting the string in2 into a textbox
    text (in2, displayWidth/2, 310*dd, 330*dd, displayHeight);

    // Display return to menu text in pink
    textSize(25*dd);
    fill(254, 69, 152);
    text("Click HERE to return to the menu!", displayWidth/2, displayHeight-40*dd);

    // If the user clicks within the last 20% of the screen, the level = 0 and the menu re-appears
    if (mousePressed) {
      if (mouseX >= 0 && mouseX <= displayWidth && mouseY >= displayHeight/1.25 && mouseY <= displayHeight) {
        level = 0;
      }
    }
}

// Game mode Level 1 
public void level1() {

  // Please refer to the classes in lines 375 and beyond for clearer details

  ti.display(); // Run the internal clock
  p.powerBar(); // Set power bar size based on the temporary score variable
  p.display(to); // Display the power bar (the constructor uses the input to determine power level) 
  h.display(ot); // Display the health bar (the constructor uses the input to determine power level)
  t.display(); // Display the black tarbit 
  t.move(0); // Move the black tarbit (the constructor uses the input to increase yspeed)
  b.display(0); // Display the white bubble (the constructor uses the input to decrease size)
  b.move(); // Move the white bubble based on the user's X position
  b.bounce(); // Make sure the white bubble does not move off the screen
  s.display(); // Display the score

  // Overlaps is a boolean constructor in the bubble class that takes tarbit t as input 
  // If the tarbit and bubble overlap, reset the tarbit and increase the score by 1
  // If they don't overlap and the tarbit's y position = the screen height, reset the tarbit and increases misses by 1

  if (b.overlaps(t) == true ) {
    t.reset();
    s.score++;
  } else if ((b.overlaps(t) == false) && (t.y >= displayHeight)) {
    t.reset();
    s.misses++;
  }

  // Increase the level by 1 if a tarbit is missed

  if (s.misses == 1) {
    level += 1;
  }
}

// Game mode Level 2
public void level2() {

  ti.display(); // Run the internal clock
  p.powerBar(); // Set power bar size based on the temporary score variable
  p.display(to); // Display the power bar (the constructor uses the input to determine power level) 
  h.display(ot); // Display the health bar (the constructor uses the input to determine power level)
  t.display(); // Display the black tarbit 
  t.move(2); // Move the black tarbit (the constructor uses the input to increase yspeed)
  b.display(5); // Display the white bubble (the constructor uses the input to decrease size)
  b.move(); // Move the white bubble based on the user's X position
  b.bounce(); // Make sure the white bubble does not move off the screen
  s.display(); // Display the score

  // Overlaps is a boolean constructor in the bubble class that takes tarbit t as input 
  // If the tarbit and bubble overlap, reset the tarbit and increase the score by 1
  // If they don't overlap and the tarbit's y position = the screen height, reset the tarbit and increases misses by 1  

  if (b.overlaps2(t) == true) {
    t.reset();
    s.tempScore++;
    s.score++;
  } else if ((b.overlaps2(t) == false) && (t.y >= height)) {
    t.reset();
    s.misses++;
  }

  // Increase the level by 1 if a tarbit is missed
  // Before the next miss occurs, if the temporary score variable = 3, decrease the level by 1 and reset the temporary score variable
  // This is so you can return your green health level to its original state through the blue power bar

  if (s.misses == 2) {
    level += 1;
  } else if (s.tempScore == 3) {
    s.misses -= 1;
    level -= 1;
    s.tempReset();
  }
}

// Game mode Level 3
public void level3() {

  ti.display(); // Run the internal clock
  p.powerBar(); // Set power bar size based on the temporary score variable
  p.display(to); // Display the power bar (the constructor uses the input to determine power level) 
  h.display(ot); // Display the health bar (the constructor uses the input to determine power level)
  t.display(); // Display the black tarbit 
  t.move(4); // Move the black tarbit (the constructor uses the input to increase yspeed)
  b.display(10); // Display the white bubble (the constructor uses the input to decrease size)
  b.move(); // Move the white bubble based on the user's X position
  b.bounce(); // Make sure the white bubble does not move off the screen
  s.display(); // Display the score

  // Overlaps is a boolean constructor in the bubble class that takes tarbit t as input 
  // If the tarbit and bubble overlap, reset the tarbit and increase the score by 1
  // If they don't overlap and the tarbit's y position = the screen height, reset the tarbit and increases misses by 1  

  if (b.overlaps3(t) == true) {
    t.reset();
    s.tempScore++;
    s.score++;
  } else if ((b.overlaps3(t) == false) && (t.y >= height)) {
    t.reset();
    s.misses++;
  }

  // Increase the level by 1 if a tarbit is missed
  // Before the next miss occurs, if the temporary score variable = 5, decrease the level by 1 and reset the temporary score variable
  // This is so you can return your green health level to its original state through the blue power bar

  if (s.misses == 3) {
    level += 1;
  } else if (s.tempScore == 5) {
    s.misses -= 1;
    level -= 1;
    s.tempReset();
  }
}

// Game mode Level 4 
public void level4() {

  ti.display(); // Run the internal clock
  p.powerBar(); // Set power bar size based on the temporary score variable
  p.display(to); // Display the power bar (the constructor uses the input to determine power level) 
  h.display(ot); // Display the health bar (the constructor uses the input to determine power level)
  t.display(); // Display the black tarbit 
  t.move(6); // Move the black tarbit (the constructor uses the input to increase yspeed)
  b.display(15); // Display the white bubble (the constructor uses the input to decrease size)
  b.move(); // Move the white bubble based on the user's X position
  b.bounce(); // Make sure the white bubble does not move off the screen
  s.display(); // Display the score

  // Overlaps is a boolean constructor in the bubble class that takes tarbit t as input 
  // If the tarbit and bubble overlap, reset the tarbit and increase the score by 1
  // If they don't overlap and the tarbit's y position = the screen height, reset the tarbit and increases misses by 1  

  if (b.overlaps4(t) == true) {
    t.reset();
    s.tempScore++;
    s.score++;
  } else if ((b.overlaps4(t) == false) && (t.y >= height)) {
    t.reset();
    s.misses++;
  }

  // When the 4th tarbit is missed, the game is over
  // Before the next miss occurs, if the temporary score variable = 7, decrease the level by 1 and reset the temp. score variable
  // This is so you can return your green health level to its original state through the blue power bar

  if (s.misses == 4) {
    level = -1;
  } else if (s.tempScore == 7) {
    s.misses -= 1;
    level -= 1;
    s.tempReset();
  }
}

// Game mode Level -1 
public void levelLose() {

    // Reset the score
    s.reset();

    // Make a black background with white text
    background(0);
    fill(255);
    textAlign(CENTER);
    textSize(200*dd);

    // Display a large skull emoji and "game over" text
    text("☠",displayWidth/2,displayHeight/3);
    textSize(53 * dd);
    text("STAGE 4 COPD", displayWidth / 2, displayHeight / 2);
    textSize(40 * dd);
    text("GAME OVER!", displayWidth / 2, displayHeight / 2 + 60 * dd);
    
    // Display highscore variable (text in pink) 
    fill(254, 69, 152);
    textSize(33 * dd);
    text("Highscore: " + s.highscore, displayWidth / 2, displayHeight / 2 + 150 * dd);
    textSize(28 * dd);
    text("Click HERE to play again!", displayWidth / 2, 40*dd);

    // If the user clicks within the top 33% of the screen, the level = 1 and the game resets
    // Internal timer is reset, power bar and health bar is reset, tarbit yspeed is reset, and temp. score variable is reset
    if (mousePressed) {
        if (mouseX >= 0 && mouseX <= displayWidth && mouseY >= 0 && mouseY <= displayHeight/3) {
            level = 1;
            ti.reset();
            startTime = millis();
            ot = 0;
            to = 0;
            t.yspeed = 8 * dd;
            s.tempScore = 0;
        }
    }
}

// Classes 

class Bubble {
  float x;
  float y; 
  float r;

  Bubble() {
    x = 0; 

    // Set y position so it is locked 
    y = displayHeight-80*dd; 

    // Set radius size in pixels
    r = 35*dd;
  }

  // Display the bubble 
  public void display(float other) {

    // Draw a white ellipse with radius - other(size of bubble decreases depending on the game state)
    ellipseMode(RADIUS);
    stroke(0);
    fill(255);
    ellipse(x, y, r-other*dd, r-other*dd);
  }

  // Move the bubble 
  public void move() {

    // The x position of the bubble depends on the position of the user's touch
    x = (mouseX);
  }

  // Make sure the white bubble does not move off the screen
  public void bounce() {

    // Reset x position of bubble to 0 or displayWidth if it goes farther than either
    if (x == displayWidth) {
      x = displayWidth;
    } else if (x == 0) {
      x = 0;
    }
  }

  // OVERLAP FOR LEVEL 1
  public boolean overlaps(Tarbit t) {

    // Calculate distance between bubble x & y and tarbit x & y 
    float d = dist(x, y, t.x, t.y);

    // If the distance between the circles is less than the bubble radius+tarbit radius, the two circles are overlapping
    if (d < r + t.r) {
      return true;
    } else {
      return false;
    }
  }

  // OVERLAP FOR LEVEL 2
  public boolean overlaps2(Tarbit t) {

    // Calculate distance between bubble x & y and tarbit x & y 
    float d2 = dist(x, y, t.x, t.y);

    // If the distance between the circles is less than the bubble radius+tarbit radius, the two circles are overlapping
    // Reduced size of radius in level 2 is also taken into consideration
    if (d2 < (r-5*dd) + t.r) {
      return true;
    } else {
      return false;
    }
  }
  // OVERLAP FOR LEVEL 3
  public boolean overlaps3(Tarbit t) {

    // Calculate distance between bubble x & y and tarbit x & y 
    float d3 = dist(x, y, t.x, t.y);

    // If the distance between the circles is less than the bubble radius+tarbit radius, the two circles are overlapping
    // Reduced size of radius in level 3 is also taken into consideration
    if (d3 < (r-10*dd) + t.r) {
      return true;
    } else {
      return false;
    }
  }
  // OVERLAP FOR LEVEL 4
  public boolean overlaps4(Tarbit t) {

    // Calculate distance between bubble x & y and tarbit x & y 
    float d4 = dist(x, y, t.x, t.y);

    // If the distance between the circles is less than the bubble radius+tarbit radius, the two circles are overlapping
    // Reduced size of radius in level 4 is also taken into consideration
    if (d4 < (r-15*dd) + t.r) {
      return true;
    } else {
      return false;
    }
  }
}

class Tarbit {
  float x;
  float y;
  float r;
  float yspeed;

  Tarbit() {

    // Tarbit x position is random between screen size
    x = random(10, displayWidth-10);
    y = 0;

    // Radius is 20 pixels and yspeed is 8 pixels
    r = 20*dd ;
    yspeed = 8*dd;
  }

  // Display the tarbit 
  public void display() {

    // Draw a black ellipse 
    ellipseMode(RADIUS);
    fill(0);
    ellipse(x, y, r, r);
  }

  // Move the tarbit and add input from constructor (other) depending on the game level (tarbits come down faster)
  public void move(float other) {
    y += yspeed + other*dd;
  }

  // Reset the tarbit 
  public void reset() {
    y = 0;
    x = random(10, displayWidth-10);
  }
}

class Health {
  float health;
  float max_health;
  float rectWidth;

  Health() {

    // Initialize health at 100%
    health = 100;
    max_health = 100;
    rectWidth = 100;
  }

  // Display health bar 

  public void display(int other) {

    // Change colour based on health (different shades of green for different health levels)
    if (health <= 25) {
      fill(0,255,127);
    } else if (health <= 50) {
      fill(127,255,0);
    } else {
      fill(0, 255, 0);
    }

    // Draw green rectangle 
    rectMode(CORNER);
    noStroke();
    // Get health/max_health and multiply it by width of bar
    float drawWidth = (health / max_health) * rectWidth;
    rect(30*dd, 30*dd, drawWidth*dd, 35*dd);

    // Draw rectangle outline 
    rectMode(CORNER);
    strokeWeight(1*dd);
    stroke(0);
    noFill();
    rect(30*dd, 30*dd, rectWidth*dd, 35*dd);

    // Decrease health depending on game level and input (constructor)
    health = 100-other;
  }
}

class Power {
  float health;
  float max_health;
  float rectWidth;

  Power() {

    // Initialize power at 0%
    health = 0;
    max_health = 100;
    rectWidth = 100;
  }

  public void display(int other) {

    // Change colour based on power level (different shades of blue for different power levels)
    if (health <= 25) {
      fill(219, 235, 250);
    } else if (health <= 50) {
      fill(135, 206, 250);
    } else {
      fill(0, 191, 255);
    }

    // Draw blue rectangle 
    rectMode(CORNER);
    noStroke();
    // Get health/max_health and multiply it by width of bar
    float drawWidth = (health / max_health) * rectWidth;
    rect(displayWidth-130*dd, 30*dd, drawWidth*dd, 35*dd);

    // Draw rectangle outline
    rectMode(CORNER);
    strokeWeight(1*dd);
    stroke(0);
    noFill();
    rect(displayWidth-130*dd, 30*dd, rectWidth*dd, 35*dd);

    // Make power equal to the input (constructor) depending on game level
    health = other;
  }

  public void powerBar() {

    // Set power bar based on temporary score variable 
    if (s.tempScore > 1) {
      to = 0; // 0
    }
    if (s.tempScore >= 1 && s.tempScore < 3) {
      to = 25; // 1 & 2
    }
    if (s.tempScore >= 3 && s.tempScore < 5) {
      to = 50; // 3 & 4
    }
    if (s.tempScore >= 5 && s.tempScore < 7) {
      to = 75; // 5 & 6
    }
    if (s.tempScore == 7) {
      to = 100; // 7
    }
  }
  // // Reset power bar
  // public void reset() {
  //   health = 0;
  // }
}

class Score {
  int score;
  int misses;
  int highscore;
  int tempScore;

  Score() {
    score = 0;
    misses = 0;
    highscore = 0;
    tempScore = 0;
  }

  // Display score with large black text 
  public void display() {
    fill(0);
    textSize(65*dd);
    text(score, displayWidth/2, 100*dd);
  }

  // Reset score and misses variables
  public void reset() {
    score = 0;
    misses = 0;
  }

  // Reset temporary Score variable and also reset power bar to 0%
  public void tempReset() {
    tempScore = 0;
    to = 0;
  }
}

class Time {
  int currentTime;

  Time() {
    currentTime = 0;
  }

  // Set the time variable
  public void display() {
    currentTime = (millis() - startTime)/1000;
  }

  // Reset time variable
  public void reset() {
    currentTime = 0;
  }
}
  // Set size of the GUI (depends on display size)
  public void settings() {  size(displayWidth, displayHeight); }
}
