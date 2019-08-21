int LED = 13;
int data = "";

void setup() {
  pinMode(LED, OUTPUT);
  Serial.begin(9600);
}

void loop() {
  while(Serial.available() > 0)
  {
    data = Serial.read();
    Serial.flush();
  }

  if(data == 'a')
  {
    digitalWrite(LED, HIGH);
    Serial.print("Data received was ");
    Serial.println(data);
    data = '4';
  }
  else if(data == 'b')
  {
    digitalWrite(LED, LOW);
    Serial.print("Data received was ");
    Serial.println(data);
    data = '4';
  }
}
