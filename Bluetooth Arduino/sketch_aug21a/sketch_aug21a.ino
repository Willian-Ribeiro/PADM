int LED = 13

String data;

void setup() {
  pinMode(LED, OUTPUT);
  Serial.begin(9600);
}

void loop() {
  while(Serial.available() > 0)
  {
    data = Serial.read();
  }

  if(data == "a")
  {
    digitalWrite(LED, HIGH);
    Serial.print("Data received was ");
    Serial.println(data);
    data = "1";
  }
  if(data == "b")
  {
    digitalWrite(LED, LOW);
    Serial.print("Data received was ");
    Serial.println(data);
    data = "1";
  }

}
