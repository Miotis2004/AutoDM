import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="toast-container" aria-live="polite" aria-atomic="true">
      @for (toast of notificationService.toasts(); track toast.id) {
        <div class="toast" [ngClass]="'toast-' + toast.type" role="alert" aria-live="assertive" aria-atomic="true">
          <div class="toast-body">
            {{ toast.message }}
            <button type="button" class="close" aria-label="Close" (click)="notificationService.removeToast(toast.id)">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
        </div>
      }
    </div>
  `,
  styles: [`
    .toast-container {
      position: fixed;
      top: 1rem;
      right: 1rem;
      z-index: 1050;
      display: flex;
      flex-direction: column;
      gap: 0.5rem;
    }
    .toast {
      min-width: 250px;
      padding: 0.75rem 1.25rem;
      border-radius: 0.25rem;
      box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
      color: #fff;
      display: flex;
      justify-content: space-between;
      align-items: center;
      opacity: 0.95;
    }
    .toast-body {
      display: flex;
      justify-content: space-between;
      width: 100%;
      align-items: center;
    }
    .toast-error {
      background-color: #dc3545;
    }
    .toast-success {
      background-color: #28a745;
    }
    .toast-warning {
      background-color: #ffc107;
      color: #212529;
    }
    .toast-info {
      background-color: #17a2b8;
    }
    .close {
      background: none;
      border: none;
      color: inherit;
      font-size: 1.5rem;
      line-height: 1;
      cursor: pointer;
      opacity: 0.8;
      margin-left: 1rem;
    }
    .close:hover {
      opacity: 1;
    }
  `]
})
export class ToastComponent {
  notificationService = inject(NotificationService);
}
