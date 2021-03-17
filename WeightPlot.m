function varargout = WeightPlot(varargin)
% WEIGHTPLOT MATLAB code for WeightPlot.fig
%      WEIGHTPLOT, by itself, creates a new WEIGHTPLOT or raises the existing
%      singleton*.
%
%      H = WEIGHTPLOT returns the handle to a new WEIGHTPLOT or the handle to
%      the existing singleton*.
%
%      WEIGHTPLOT('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in WEIGHTPLOT.M with the given input arguments.
%
%      WEIGHTPLOT('Property','Value',...) creates a new WEIGHTPLOT or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before WeightPlot_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to WeightPlot_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help WeightPlot

% Last Modified by GUIDE v2.5 28-Apr-2019 23:38:32

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @WeightPlot_OpeningFcn, ...
                   'gui_OutputFcn',  @WeightPlot_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT

% --- Executes just before WeightPlot is made visible.
function WeightPlot_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to WeightPlot (see VARARGIN)

% Choose default command line output for WeightPlot
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% This sets up the initial plot - only do when we are invisible
% so window can get raised using WeightPlot.
if strcmp(get(hObject,'Visible'),'off')%
    hObject.Visible = 'on';
 end

% UIWAIT makes WeightPlot wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = WeightPlot_OutputFcn(hObject, eventdata, handles)
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;

% --- Executes on button press in pushbutton1.
function pushbutton1_Callback(hObject, eventdata, handles)
% hObject    handle to pushbutton1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
global state
global maze
if ~isfield(state,'weightings')
  state.weightings = containers.Map;
end
if isempty(state.weightings)
  return;
end
axes(handles.axes1);
cla;

try
  popup_sel_indices = get(handles.listbox1, 'Value');
  popup_sel_keys = arrayfun( @(index) handles.listbox1.String{index}, popup_sel_indices, 'UniformOutput', false);

  % Plot each of the weighting keys
  hold off;
  p = gobjects(numel(popup_sel_keys), 1);
  pcount = 0;
  level = 0;
  for key = popup_sel_keys
    pcount = pcount + 1;
    data = state.weighting(key)
    % Normalize curves?;
    if handles.togglebutton_normalize.Value
      data = data / sum(data); % normalize area under curve to unity
    end
    % Stack curves?
    if handles.togglebuton_stack.Value
      level = level + sum(data);
    end
    if numel(data) == numel(maze.platforms)
      p(pcount) = func( maze.platforms, data + level);
    else
      p(pcount) = func( 1:numel(data), data + level);
    end
    hold on;
  end
  legend( p, popup_sel_keys);
catch exception
  warndlg( str(ME), 'Plot Issue')
end


% --------------------------------------------------------------------
function FileMenu_Callback(hObject, eventdata, handles)
% hObject    handle to FileMenu (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)


% --------------------------------------------------------------------
function OpenMenuItem_Callback(hObject, eventdata, handles)
% hObject    handle to OpenMenuItem (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
file = uigetfile('*.fig');
if ~isequal(file, 0)
    open(file);
end

% --------------------------------------------------------------------
function PrintMenuItem_Callback(hObject, eventdata, handles)
% hObject    handle to PrintMenuItem (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
printdlg(handles.figure1)

% --------------------------------------------------------------------
function CloseMenuItem_Callback(hObject, eventdata, handles)
% hObject    handle to CloseMenuItem (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
selection = questdlg(['Close ' get(handles.figure1,'Name') '?'],...
                     ['Close ' get(handles.figure1,'Name') '...'],...
                     'Yes','No','Yes');
if strcmp(selection,'No')
    return;
end

delete(handles.figure1)


% --- Executes on selection change in popupmenu1.
function popupmenu1_Callback(hObject, eventdata, handles)
% hObject    handle to popupmenu1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
global state
global maze

% Hints: contents = get(hObject,'String') returns popupmenu1 contents as cell array
%        contents{get(hObject,'Value')} returns selected item from popupmenu1


% --- Executes during object creation, after setting all properties.
function popupmenu1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to popupmenu1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: popupmenu controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
     set(hObject,'BackgroundColor','white');
end

global state
global maze
hObject.String = state.weighting.keys();


% --- Executes on selection change in listbox1.
function listbox1_Callback(hObject, eventdata, handles)
% hObject    handle to listbox1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: contents = cellstr(get(hObject,'String')) returns listbox1 contents as cell array
%        contents{get(hObject,'Value')} returns selected item from listbox1
global state
if ~isfield(state,'weightings')
  state.weightings = containers.Map;
end
keys = state.weightings.keys();
if ~isequal(keys, hObject.String)
  hObject.String = keys;
end

% --- Executes during object creation, after setting all properties.
function listbox1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to listbox1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: listbox controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end

global state
keys = state.weightings.keys();
if ~isequal(keys, hObject.String)
  hObject.String = keys;
end

% --- Executes on button press in togglebutton_stack.
function togglebutton_stack_Callback(hObject, eventdata, handles)
% hObject    handle to togglebutton_stack (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of togglebutton_stack


% --- Executes on button press in togglebutton_normalize.
function togglebutton_normalize_Callback(hObject, eventdata, handles)
% hObject    handle to togglebutton_normalize (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of togglebutton_normalize


% --- Executes during object creation, after setting all properties.
function axes1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to axes1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: place code in OpeningFcn to populate axes1
text(hObject,1,1,'Select up to 6 weighting datasets to plot!');
