import {Component, OnInit, Input, OnChanges} from "@angular/core";
import {TestDetailService} from "./test-detail.service";

@Component({
  selector: 'app-test-detail',
  templateUrl: './test-detail.component.html',
  styleUrls: ['./test-detail.component.css'],
  providers: [TestDetailService]
})
export class TestDetailComponent implements OnInit, OnChanges {
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
    this.testDetailService.getTestDetail(this.token)
      .subscribe(data => {
        console.log((data as any)._body);
        this.testDetail = JSON.parse((data as any)._body);
      }, error => {
        console.log(JSON.stringify(error.json()));
      });
  }

  ngOnChanges() {
    this.ngOnInit();
  }
}
