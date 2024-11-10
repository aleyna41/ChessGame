import {AfterViewInit, Component, ElementRef, OnInit, Renderer2, ViewChild} from '@angular/core';
import * as confetti from 'canvas-confetti';
import {GameService} from "../../Service/game.service";
import {ActivatedRoute} from "@angular/router";

// Code für Konfetti abgeändert von:
// https://lucyveron.com/2021/02/09/you-should-add-%F0%9F%8E%89-canvas-confetti-%F0%9F%8E%89-to-your-angular-project/

@Component({
    selector: 'app-game-result',
    templateUrl: './game-result.component.html',
    styleUrls: ['./game-result.component.css']
})
export class GameResultComponent implements OnInit, AfterViewInit {

    @ViewChild('confettiContainer') confettiContainer!: ElementRef;
    confettiTriggered = false;
    eloWinnerPoints: number = 0;
    eloLooserPoints: number = 0;
    winnerName: string = '';
    looserName: string = '';
    gameId: number = 0;

    constructor(private renderer2: Renderer2, private gameService: GameService, private route: ActivatedRoute) {
      this.gameService = gameService
    }

    ngOnInit(): void {
      this.gameId = +this.route.snapshot.paramMap.get('gameId')!;
      this.gameService.getWinnerAndElo(this.gameId).subscribe(data => {
        const winnerEntry = Object.entries(data)[0];
        this.winnerName = winnerEntry[0];
        this.eloWinnerPoints = winnerEntry[1]-10;
      });

      this.gameService.getLooserAndElo(this.gameId).subscribe(data => {
        const looserEntry = Object.entries(data)[0];
        this.looserName = looserEntry[0];
        this.eloLooserPoints = looserEntry[1]+10;
      });
      setTimeout(() => {
          this.updatePoints();
      }, 3000);

    }

    ngAfterViewInit(): void {
        console.log('ngAfterViewInit is called!');
        this.triggerConfetti(this.createDummyMouseEvent());
    }

    private updatePoints(): void {
        this.eloWinnerPoints += 10;
        this.eloLooserPoints -= 10;
    }

    public triggerConfetti(event: MouseEvent): void {
        if (!this.confettiTriggered) {
            const canvas = this.renderer2.createElement('canvas');
            this.renderer2.appendChild(this.confettiContainer.nativeElement, canvas);

            const containerRect = this.confettiContainer.nativeElement.getBoundingClientRect();
            const offsetX = containerRect.width / 2 - 180;
            const offsetY = containerRect.height / 2 - 200;

            this.renderer2.setStyle(canvas, 'position', 'absolute');
            this.renderer2.setStyle(canvas, 'top', `${offsetY}px`);
            this.renderer2.setStyle(canvas, 'left', `${offsetX}px`);
            this.renderer2.setStyle(canvas, 'width', '100%');
            this.renderer2.setStyle(canvas, 'height', '100%');

            const myConfetti = confetti.create(canvas, {
                resize: true
            });

            myConfetti();

            this.confettiTriggered = false;
        }
    }

    private createDummyMouseEvent(): MouseEvent {
        return new MouseEvent('mouseover', {
            bubbles: true,
            cancelable: true,
            clientX: 0,
            clientY: 0,
        });
    }
}
