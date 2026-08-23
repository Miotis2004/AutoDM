import { TestBed } from '@angular/core/testing';
import { AppShellComponent } from './app-shell.component';
import { provideRouter } from '@angular/router';

describe('AppShellComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppShellComponent],
      providers: [provideRouter([])]
    }).compileComponents();
  });

  it('should create the component', () => {
    const fixture = TestBed.createComponent(AppShellComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });
});
