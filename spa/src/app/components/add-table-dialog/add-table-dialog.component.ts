import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'ecs-add-table-dialog',
  templateUrl: './add-table-dialog.component.html',
  styleUrls: ['./add-table-dialog.component.css']
})
export class AddTableDialogComponent implements OnInit {

  public name = "";

  constructor() { }

  ngOnInit(): void {
  }

}
