import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {  pulseAnimation } from 'src/app/animations';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css'],
  animations:[
    
    pulseAnimation,
    trigger('fadeInAnimation', [
      state('void', style({ opacity: 0, transform: 'translateY(100%)' })),
      transition('void => *', animate('400ms 0ms cubic-bezier(0.25, 0.8, 0.25, 1)')),
    ]),

    trigger('imageSizeAnimation', [
      state('small', style({height:'30vh'})),
      transition('* <=> small', animate('400ms 0ms cubic-bezier(0.25, 0.8, 0.25, 1)'))
    ])
  ]
})
export class CardComponent implements OnInit {

  @Input() title : any
  @Input() image : any
  @Input() description : any

  onHover : boolean 
  cardState: 'initial' | 'pulse' = 'initial';
  contentState: 'void' | '*' = 'void';
  imageSizeState : 'initial'| 'small' = 'initial'

  constructor() {
    this.onHover = false
   }

  ngOnInit(): void {
  }

  startAnimation() {
    this.cardState = 'pulse';
    this.contentState = '*';
    this.imageSizeState = 'small'
    this.onHover = true
  }

  stopAnimation() {
    this.cardState = 'initial';
    this.contentState = 'void';
    this.imageSizeState = 'initial'
    this.onHover = false

}
}