import {Component, OnInit, Input} from '@angular/core';
import {ModuledetailService} from "./moduledetail.service";
import { TreeModule } from 'angular2-tree-component';
import 'rxjs/Rx';

@Component({
  selector: 'app-moduledetail',
  templateUrl: './moduledetail.component.html',
  styleUrls: ['./moduledetail.component.css'],
  providers: [ModuledetailService, TreeModule]
})
export class ModuledetailComponent implements OnInit {
  @Input()
  module: string;
  nodes = null;
  counter: number;
  currentNode = null;

  constructor(
    private moduleDetailService: ModuledetailService
  ) {
    this.counter = 0;
  }

  ngOnInit() {
    this.nodes = this.moduleDetailService.getModuleTree(this.module);
  }

  onSelectedNode($event) {
    if(this.counter++ % 2 === 0) { // tree module library is buggy and triggers event twice as needed
                                   // this hack is needed to navigate through tree properly
      if($event.node.data.token === undefined) {
        $event.node.treeModel.focusDrillDown();
      } else {
        console.log($event.node.data.name);
        this.currentNode = $event.node;
      }
    }
  }
}
