import * as React from "react";
import * as TooltipPrimitive from "@radix-ui/react-tooltip";

export const TooltipProvider = TooltipPrimitive.Provider;
export const Tooltip = TooltipPrimitive.Root;
export const TooltipTrigger = TooltipPrimitive.Trigger;

export const TooltipContent = React.forwardRef<
  React.ElementRef<typeof TooltipPrimitive.Content>,
  React.ComponentPropsWithoutRef<typeof TooltipPrimitive.Content>
>(({ children, sideOffset = 6, ...props }, ref) => (
  <TooltipPrimitive.Content
    ref={ref}
    sideOffset={sideOffset}
    className="z-50 rounded-md bg-black/90 px-2 py-1 text-xs text-white shadow-md"
    {...props}
  >
    {children}
    <TooltipPrimitive.Arrow className="fill-black/90" />
  </TooltipPrimitive.Content>
));
TooltipContent.displayName = "TooltipContent";
