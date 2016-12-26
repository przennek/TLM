import {Component, OnInit, Input} from '@angular/core';
import {ModuledetailService} from "./moduledetail.service";

@Component({
  selector: 'app-moduledetail',
  templateUrl: './moduledetail.component.html',
  styleUrls: ['./moduledetail.component.css'],
  providers: [ModuledetailService]
})
export class ModuledetailComponent implements OnInit {
  @Input()
  module: string;

  constructor(
    private moduleDetailService: ModuledetailService
  ) { }

  ngOnInit() {
    console.log(this.moduleDetailService.getModuleTree(this.module))
  }

}
