import { Component, Input, OnInit } from '@angular/core';
import { AnimationOptions } from 'ngx-lottie';

@Component({
  selector: 'app-lottie-animation-view',
  template: `
    <ng-lottie
      [options]="animationOptions"
      [width]= "iconWidth"
      [height]="iconHeight"
    ></ng-lottie>
  `,
})
export class LottieAnimationViewComponent implements OnInit {
  
  @Input() animationPath: string = '';
  @Input() iconWidth: string = '20';
  @Input() iconHeight: string = '20';

  animationOptions: AnimationOptions = {
    path: '',
    autoplay: true,
    loop: false,
  };

  ngOnInit(): void {
    this.animationOptions = {
      ...this.animationOptions,
      path : `/assets/lotties/${this.animationPath}`
    };
  }
}
