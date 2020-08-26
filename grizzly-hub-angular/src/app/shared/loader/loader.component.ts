import { Component, OnInit } from '@angular/core';
import { LoaderService } from './loader.service';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.scss']
})
export class LoaderComponent implements OnInit {

  color = 'primary';
  mode = 'indeterminate';
  value = 50;
  isLoading: Subject<boolean> = this.loaderService.isLoading;
  show = false;

  constructor(private loaderService: LoaderService) { }

  ngOnInit() {
    this.isLoading.subscribe(status => this.show = status);
  }


}
