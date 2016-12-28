import { Component, OnInit, Input } from '@angular/core';
import {TestDetailService} from "./test-detail.service";

@Component({
  selector: 'app-test-detail',
  templateUrl: './test-detail.component.html',
  styleUrls: ['./test-detail.component.css'],
  providers: [TestDetailService]
})
export class TestDetailComponent implements OnInit {
  @Input()
  node;

  token: string;
  name: string;
  testDetail;

  constructor(
    private testDetailService: TestDetailService
  ) {}

  ngOnInit() {
    this.token = this.node.data.token;
    this.name = this.node.data.name;
    this.testDetail = this.testDetailService.getTestDetail(this.token);
    console.log(this.testDetail)
    console.log(this.testDetail.classComment)
  }

}
