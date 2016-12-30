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
  runLog;

  constructor(
    private testDetailService: TestDetailService
  ) {}

  ngOnInit() {
    this.token = this.node.data.token;
    this.name = this.node.data.name;
    this.testDetailService.getTestDetail(this.token)
      .subscribe(data => {
        console.log((data as any)._body);
        const result = JSON.parse((data as any)._body);
        this.runLog = this.parseRunLog(result.editLog);
        this.testDetail = JSON.parse(result.jsonData);
      }, error => {
        console.log(JSON.stringify(error.json()));
      });
  }

  ngOnChanges() {
    this.ngOnInit();
  }

  private parseRunLog(runLog: Array<string>) {
    let resultRunLog = [];
    runLog.forEach(x => {
      let log = JSON.parse(x);
      log.date = this.timestampToDate(log.date);
      resultRunLog.push(log);
    });
    return resultRunLog;
  }


  private timestampToDate(timestamp: number) {
    return new Date(timestamp);
  }
}
