# Game of Life w/Swing-based GUI
Game of Life simulation in Java visualized with Swing

## TODO:
- [ ] better documentation

- [x] ~~complete translation~~
  - [x] ~~Main.java~~
  - [x] ~~Controller.java~~
  - [x] ~~Model.java~~
  - [x] ~~View.java~~
  - [x] ~~Cell.java~~
- [ ] in general: more thread optimization?
  - [ ] thread optimization in Model
  - [x] ~~thread optimization in View (init)~~
- [x] ~~fix update loop (move wait from main thread)~~
- [x] ~~fix update loop pt2 (more thread optimization?)~~
- [ ] implement more user options
  - [ ] custom size
  - [ ] fullscreen button
  - [x] ~~add repopulate button (generate new 1st/0th gen)~~
    - [x] ~~(bonus objective) rename from "Reset" to "Regen"?~~
  - [x] ~~add clear button (kills all living cells)~~
- [ ] more???
- [ ] edit styling(??)

## BUGS?
(fixed if checked)
- [x] ~~cell update~~
  - Symptom: cells (seemingly?) not behaving correctly during update (known self-sustainable patterns not working as expected)
  - Cause: seems to be multithreading during grid-building in the gui, causing a discrepancy between grid representations in View and Model 
  - Fix: remove parallelization during grid-setup in View.init (bug-causing section reverted to fbe21a0dfca18cfa8cd0ec15f861deb0baf28613)
