# OOP-SP2026-final-project-Expense-Tracker
Repository for all UML graphs &amp; designs, as well as code for our OOP final project.

# UML Diagrams
In this repository, all UML diagrams are written as PlantUML code. In order to render PlantUML in VS Code:
1. Go to Extensions
2. Search for "PlantUML" (by jebbs)
3. Install it 
4. (Additionally) install OpenJDK (if you don't hav it)

How to represent "screens"/UIs WITHOUT JFrame UIs?
- Class per screen? (simulate a JFrame??)
- Class Main contains methods for each screen?

Sample System Workflow:
1. authentication, as authUI  
a. findUser() -->  
    if not found: update UI
    else: "close" self, open mainUI
b. show()'
2. main UI / platform dashboard
3. mainUI ◇--> Expense (via List)

